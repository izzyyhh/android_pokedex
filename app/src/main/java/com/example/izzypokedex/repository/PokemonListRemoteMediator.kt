package com.example.izzypokedex.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.izzypokedex.api.ApiMapper
import com.example.izzypokedex.api.PokeApi
import com.example.izzypokedex.api.getIdFromUrl
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.entities.DbPokemon
import kotlinx.coroutines.*
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonListRemoteMediator @Inject constructor(
    private val pokeApi: PokeApi,
    private val pokemonDao: PokemonDao,
    private val apiMapper: ApiMapper,
    private val dbMapper: DbMapper
): RemoteMediator<Int, DbPokemon>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DbPokemon>
    ): MediatorResult {
        val pokeTableSize = pokemonDao.size()

        val offset = when(loadType) {
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.REFRESH -> 0
            LoadType.APPEND -> pokeTableSize
        }

        // poke api get list
        val apiListInfo = pokeApi.getPokemonList(
            limit = when(loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                else -> state.config.pageSize
            },
            offset = offset
        )

        // insert new data into db
        coroutineScope {
            val apiPokes = apiListInfo.results.map {
                val pokeId = getIdFromUrl(it.url)
                pokeApi.getPokemon(pokeId)
            }

            val pokemon = apiPokes.map {
                async { apiMapper.mapToDomainModel(it) }
            }.awaitAll()

            pokemon.forEach {
                if (it != null) {
                    async {  pokemonDao.insert(dbMapper.mapToEntity(it))}
                }
            }
        }

        return MediatorResult.Success(
            endOfPaginationReached = pokeTableSize == apiListInfo.count
        )
    }

    override suspend fun initialize(): InitializeAction {
        return if(pokemonDao.size() > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}

