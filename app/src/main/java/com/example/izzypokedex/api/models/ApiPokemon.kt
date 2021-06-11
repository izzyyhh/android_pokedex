package com.example.izzypokedex.api.models

import androidx.compose.ui.text.font.FontWeight
import com.squareup.moshi.Json
import java.util.*

data class ApiPokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites = Sprites(),
    val types: List<TypeEntry> = emptyList<TypeEntry>(),
    val height: Int = 0,
    val weight: Int = 0,
    val stats: List<StatEntry> = emptyList()
)

data class Sprites(
    @Json(name = "front_shiny") val frontShiny: String? = "",
    val other: Other =  Other()
)

data class Other(
    @Json(name = "official-artwork")
    val officialArtwork: OfficialArtwork = OfficialArtwork()
)

data class OfficialArtwork(
    @Json(name = "front_default") val frontDefault: String? = ""
)

data class TypeEntry(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)

data class StatEntry(
    @Json(name = "base_stat") val baseStat: Int,
    val stat: Stat,
)

data class Stat(
    val name: String,
)