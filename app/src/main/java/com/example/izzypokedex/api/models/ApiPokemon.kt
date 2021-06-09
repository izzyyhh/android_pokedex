package com.example.izzypokedex.api.models

import com.squareup.moshi.Json
import java.util.*

data class ApiPokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites = Sprites(),
    val types: List<TypeEntry> = emptyList<TypeEntry>()
)

data class Sprites(
    @Json(name = "front_shiny") val frontShiny: String = "",
    val other: Other =  Other()
)

data class Other(
    @Json(name = "official-artwork")
    val officialArtwork: OfficialArtwork = OfficialArtwork()
)

data class OfficialArtwork(
    @Json(name = "front_default") val frontDefault: String = ""
)

data class TypeEntry(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)