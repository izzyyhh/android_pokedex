package com.example.izzypokedex.db.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.izzypokedex.db.entities.DbPokemon

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    suspend fun get(): List<DbPokemon>

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getPaging(): PagingSource<Int, DbPokemon>

    @Query("SELECT COUNT(id) FROM pokemon")
    suspend fun size(): Int

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun get(id: Int): DbPokemon

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: DbPokemon)
}