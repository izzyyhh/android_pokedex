package com.example.izzypokedex.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.daos.PokemonSpeciesDao
import com.example.izzypokedex.db.entities.DbPokemon
import com.example.izzypokedex.db.entities.DbPokemonSpecies

@Database(
    entities = [DbPokemon::class, DbPokemonSpecies::class],
    exportSchema = false,
    version = 4
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun dao(): PokemonDao
    abstract fun speciesDao(): PokemonSpeciesDao

    companion object{
        val DATABSE_NAME: String = "pokemon_db"
    }
}