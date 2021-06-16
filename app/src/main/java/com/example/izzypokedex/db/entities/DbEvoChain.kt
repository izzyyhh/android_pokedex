package com.example.izzypokedex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_evo")
data class DbEvoChain(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val stage1: Int?,
    val stage2: Int?,
    val stage3: Int?
)
