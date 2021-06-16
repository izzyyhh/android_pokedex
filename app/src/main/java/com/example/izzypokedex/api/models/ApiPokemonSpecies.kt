package com.example.izzypokedex.api.models

import com.squareup.moshi.Json

data class ApiPokemonSpecies(
    @Json(name="evolution_chain") val evolutionChain : EvolutionChain,
    @Json(name="flavor_text_entries") val textEntries : List<TextEntry>,
    @Json(name = "base_happiness") val happiness: Int,
    @Json(name = "capture_rate") val captureRate: Int,
    val color: Color,
    val id: Int
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