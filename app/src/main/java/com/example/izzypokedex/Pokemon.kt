package com.example.izzypokedex

data class Pokemon(
    val id: Int,
    val name: String,
    val frontShiny: String,
    val frontOfficialDefault: String,
    val types: List<String>,
    val onClick: () -> Unit = {},
    val species: PokemonSpecies = PokemonSpecies(),
    val height: Int = 0,
    val weight: Int = 0,
    val stats: Stats =  Stats(),
    val evolution: List<Pokemon> = emptyList()
)

data class PokemonSpecies(
    val color: String = "",
    val text: String = ""
)

data class Stats(
    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int = 0,
    val speed: Int = 0
)