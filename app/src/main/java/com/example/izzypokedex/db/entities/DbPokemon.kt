package com.example.izzypokedex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class DbPokemon(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val frontShiny: String,
    val frontOfficialDefault: String,
    val type1: String,
    val type2: String,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)
