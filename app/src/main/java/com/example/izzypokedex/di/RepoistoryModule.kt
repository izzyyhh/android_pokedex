package com.example.izzypokedex.di

import com.example.izzypokedex.api.ApiMapper
import com.example.izzypokedex.api.PokeApi
import com.example.izzypokedex.db.DbMapper
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.daos.PokemonEvoDao
import com.example.izzypokedex.db.daos.PokemonSpeciesDao
import com.example.izzypokedex.repository.PokemonListRemoteMediator
import com.example.izzypokedex.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoistoryModule {

    @Provides
    fun provideMainRepository(
        pokemonDao: PokemonDao,
        dbMapper: DbMapper,
        apiMapper: ApiMapper,
        pokeApi: PokeApi,
        pokemonSpeciesDao: PokemonSpeciesDao,
        pokemonEvoDao: PokemonEvoDao
    ) : PokemonRepository =  PokemonRepository(pokemonDao, pokeApi, dbMapper, apiMapper, pokemonSpeciesDao, pokemonEvoDao)
}