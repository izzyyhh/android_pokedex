package com.example.izzypokedex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_species")
data class DbPokemonSpecies(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val text: String,
    val color: String,
    val evoChainId: Int,
)