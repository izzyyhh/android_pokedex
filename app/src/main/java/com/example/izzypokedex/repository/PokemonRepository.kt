package com.example.izzypokedex.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.api.ApiMapper
import com.example.izzypokedex.api.PokeApi
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

// single source of truth: database
class PokemonRepository
@Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokeApi: PokeApi,
    private val dbMapper: DbMapper,
    private val apiMapper: ApiMapper
){
    suspend fun getPokemon(): Flow<DataState<List<Pokemon>>> = flow {
        emit(DataState.Loading)

        try {
            val apiPokemon = pokeApi.getPokemon(25)
            val poke = apiMapper.mapToDomainModel(apiPokemon)
            //insert into database here
            pokemonDao.insert(dbMapper.mapToEntity(poke))
            val cachedPokes = pokemonDao.get()
            val pokes = cachedPokes.map {
                dbMapper.mapToDomainModel(it)
            }

            emit(DataState.Success(pokes))


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

    @ExperimentalPagingApi
    fun getPaging(size: Int) = Pager(
        config = PagingConfig(size),
        pagingSourceFactory = { pokemonDao.getPaging() },
        remoteMediator = PokemonListRemoteMediator(pokeApi = pokeApi, pokemonDao = pokemonDao, apiMapper = apiMapper, dbMapper = dbMapper)
    ).flow.map {
        it.map{ dbPokemon -> dbMapper.mapToDomainModel(entity = dbPokemon)}
    }

}