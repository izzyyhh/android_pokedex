package com.example.izzypokedex.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.izzypokedex.db.entities.DbPokemonSpecies

@Dao
interface PokemonSpeciesDao {
    @Query("SELECT * FROM pokemon_species WHERE id = :id")
    suspend fun get(id: Int): DbPokemonSpecies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: DbPokemonSpecies)
}