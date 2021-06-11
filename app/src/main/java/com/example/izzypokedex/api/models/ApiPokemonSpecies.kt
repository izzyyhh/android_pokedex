package com.example.izzypokedex.api.models

import com.squareup.moshi.Json

data class ApiPokemonSpecies(
    @Json(name="evolution_chain") val evolutionChain : EvolutionChain,
    @Json(name="flavor_text_entries") val textEntries : List<TextEntry>,
    val color: Color
)

data class EvolutionChain(
    val url: String
)

data class TextEntry(
    @Json(name = "flavor_text") val text: String,
    val language: Language
)

data class Language(
    val name: String
)

data class Color(
    val name: String
)