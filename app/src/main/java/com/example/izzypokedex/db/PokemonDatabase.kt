package com.example.izzypokedex.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.izzypokedex.db.daos.PokemonDao
import com.example.izzypokedex.db.entities.DbPokemon

@Database(entities = [DbPokemon::class], exportSchema = false, version = 3)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun dao(): PokemonDao

    companion object{
        val DATABSE_NAME: String = "pokemon_db"
    }
}