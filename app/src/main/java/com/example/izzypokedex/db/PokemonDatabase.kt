package com.example.izzypokedex.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.daos.PokemonEvoDao
import com.example.izzypokedex.db.daos.PokemonSpeciesDao
import com.example.izzypokedex.db.entities.DbEvoChain
import com.example.izzypokedex.db.entities.DbPokemon
import com.example.izzypokedex.db.entities.DbPokemonSpecies

@Database(
    entities = [DbPokemon::class, DbPokemonSpecies::class, DbEvoChain::class],
    exportSchema = false,
    version = 1
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun dao(): PokemonDao
    abstract fun speciesDao(): PokemonSpeciesDao
    abstract fun evoDao(): PokemonEvoDao

    companion object{
        const val DATABASE_NAME: String = "pokemon_db"
    }
}