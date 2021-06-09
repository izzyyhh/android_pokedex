package com.example.izzypokedex

data class Pokemon(
    val id: Int,
    val name: String,
    val frontShiny: String,
    val frontOfficialDefault: String,
    val types: List<String>,
    val onClick: () -> Unit = {}
)
