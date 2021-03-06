package com.example.izzypokedex.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.*
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.PokemonSpecies
import com.example.izzypokedex.Stats
import com.example.izzypokedex.api.ApiMapper
import com.example.izzypokedex.api.PokeApi
import com.example.izzypokedex.api.getIdFromUrl
import com.example.izzypokedex.api.models.ApiPokeEvoChain
import com.example.izzypokedex.api.models.ApiPokemon
import com.example.izzypokedex.api.models.ApiPokemonSpecies
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.daos.PokemonEvoDao
import com.example.izzypokedex.db.daos.PokemonSpeciesDao
import com.example.izzypokedex.db.entities.DbEvoChain
import com.example.izzypokedex.db.entities.DbPokemon
import com.example.izzypokedex.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.Exception

// single source of truth: database
class PokemonRepository
@Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokeApi: PokeApi,
    private val dbMapper: DbMapper,
    private val apiMapper: ApiMapper,
    private val pokemonSpeciesDao: PokemonSpeciesDao,
    private val pokemonEvoDao: PokemonEvoDao
){
    @ExperimentalPagingApi
    fun getPaging(size: Int): Flow<PagingData<Pokemon>> = Pager(
        config = PagingConfig(pageSize = size),
        pagingSourceFactory = { pokemonDao.getPaging() },
        remoteMediator = PokemonListRemoteMediator(pokeApi = pokeApi, pokemonDao = pokemonDao, apiMapper = apiMapper, dbMapper = dbMapper)
    ).flow.map {
        it.map{ dbPokemon -> dbMapper.mapToDomainModel(entity = dbPokemon)}
    }

    suspend fun getPokemon(pokeId: Int): Flow<DataState<Pokemon>> = flow {
        emit(DataState.Loading)

        try {
            if(pokeNeedsToBeFetched(pokeId)) {
                val apiPokemon = pokeApi.getPokemon(pokeId)
                val poke = apiMapper.mapToDomainModel(apiPokemon)
                //insert into database here
                pokemonDao.insert(dbMapper.mapToEntity(poke))
            } else {
                emit(DataState.Success(dbMapper.mapToDomainModel(pokemonDao.get(pokeId) as DbPokemon)))
            }

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getPokemons(size: Int): Flow<DataState<List<Pokemon>>> = flow {
        emit(DataState.Loading)

        try {
            val apiPokemons = List(size) {
                pokeApi.getPokemon(it + 1)
            }

            apiMapper.mapToDomainModelList(apiPokemons).forEach {
                pokemonDao.insert(dbMapper.mapToEntity(it))
            }

            emit(DataState.Success(dbMapper.mapToDomainModelList(pokemonDao.get())))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getDetailPokemon(id: Int): Flow<DataState<Pokemon>>  = flow {
        emit(DataState.Loading)

        try {
            val apiSpecies: ApiPokemonSpecies = pokeApi.getPokemonSpecies(id)
            val apiPoke: ApiPokemon = pokeApi.getPokemon(id)
            val evoChainId = getIdFromUrl(apiSpecies.evolutionChain.url)

            // TODO("persist like evochain for better caching")
            pokemonSpeciesDao.insert(apiMapper.mapApiSpeciesToDbSpecies(apiSpecies))

            val evoChain = if(evoNeedsToBeFetched(evoChainId)) fetchAndPersistEvoChain(evoChainId) else getEvoChainFromDb(evoChainId)

            emit(DataState.Success(Pokemon(
                id = id,
                name = apiPoke.name,
                frontShiny = apiPoke.sprites.frontShiny ?: "",
                frontOfficialDefault = apiPoke.sprites.other.officialArtwork.frontDefault ?: "",
                species = PokemonSpecies(
                    color = apiSpecies.color.name,
                    text = apiSpecies.textEntries.filter { it.language.name == "en" }[0].text
                ),
                types = apiPoke.types
                    .map{
                        it.type.name
                    },
                height = apiPoke.height,
                weight = apiPoke.weight,
                stats = Stats(
                    hp = apiPoke.stats.first{ it.stat.name == "hp"}.baseStat,
                    attack = apiPoke.stats.first{ it.stat.name == "attack"}.baseStat,
                    defense = apiPoke.stats.first{ it.stat.name == "defense"}.baseStat,
                    specialAttack = apiPoke.stats.first{ it.stat.name == "special-attack"}.baseStat,
                    specialDefense = apiPoke.stats.first{ it.stat.name == "special-defense"}.baseStat,
                    speed = apiPoke.stats.first{ it.stat.name == "speed"}.baseStat
                ),
                evolution = evoChain,
                happiness = apiSpecies.happiness,
                captureRate = apiSpecies.captureRate
            )))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    private suspend fun fetchAndPersistEvoChain(evoChainId: Int): List<Pokemon> {
        val apiEvo: ApiPokeEvoChain = pokeApi.getPokeEvoChain(evoChainId)
        pokemonEvoDao.insert(apiMapper.mapApiEvoToDbEvo(apiEvo))

        try {
            val chain = mutableListOf<Int>()
            chain.add(getIdFromUrl(apiEvo.chain.species.url))

            var evoData = apiEvo.chain.evolvesTo

            while (true) {
                chain.add(getIdFromUrl(evoData[0].species.url))

                evoData = evoData[0].evolvesTo!!

                if (evoData.isEmpty()) break
            }

            return chain.map{
                apiMapper.mapToDomainModel(pokeApi.getPokemon(it))
            }

        } catch (e: Exception) {
            return emptyList()
        }
    }

    private suspend fun fetchAndPersistPokemon(id: Int) {
        val apiPokemon = pokeApi.getPokemon(id)
        val poke = apiMapper.mapToDomainModel(apiPokemon)
        pokemonDao.insert(dbMapper.mapToEntity(poke))
    }

    private suspend fun getEvoChainFromDb(evoChainId: Int): List<Pokemon> {
        val evoChain: DbEvoChain = pokemonEvoDao.get(evoChainId) as DbEvoChain
        val evoChainList = listOfNotNull(
            evoChain.stage1,
            evoChain.stage2,
            evoChain.stage3
        )

        evoChainList.forEach {
            if (pokeNeedsToBeFetched(it)) {
                fetchAndPersistPokemon(it)
            }
        }

        return evoChainList.map {
            dbMapper.mapToDomainModel(pokemonDao.get(it) as DbPokemon)
        }
    }

    private suspend fun pokeNeedsToBeFetched(id: Int): Boolean = pokemonDao.get(id) == null
    private suspend fun evoNeedsToBeFetched(id: Int): Boolean = pokemonEvoDao.get(id) == null
    private suspend fun speciesNeedsToBeFetched(id: Int): Boolean = pokemonSpeciesDao.get(id) == null
}


