package com.example.izzypokedex.di

import android.content.Context
import androidx.room.Room
import com.example.izzypokedex.db.PokemonDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.daos.PokemonSpeciesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Singleton
    @Provides
    fun database(@ApplicationContext context: Context) : PokemonDatabase = Room
        .databaseBuilder(context, PokemonDatabase::class.java, PokemonDatabase.DATABSE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase) : PokemonDao = pokemonDatabase.dao()

    @Singleton
    @Provides
    fun providePokemonSpeciesDao(pokemonDatabase: PokemonDatabase) : PokemonSpeciesDao = pokemonDatabase.speciesDao()
}