package com.example.izzypokedex.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.izzypokedex.db.entities.DbEvoChain

@Dao
interface PokemonEvoDao {
    @Query("SELECT * FROM pokemon_evo WHERE id = :id")
    suspend fun get(id: Int): DbEvoChain

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg evoChain: DbEvoChain)
}