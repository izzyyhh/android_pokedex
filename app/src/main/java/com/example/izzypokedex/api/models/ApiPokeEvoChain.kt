package com.example.izzypokedex.api.models

import com.squareup.moshi.Json

data class ApiPokeEvoChain(
    val chain: EvoChain
)

data class EvoChain(
    @Json(name = "evolves_to") val evolvesTo : List<EvolvesToEntry>,
    val species: SpeciesEntry
)

data class EvolvesToEntry(
    val species: SpeciesEntry,
    @Json(name = "evolves_to") val evolvesTo: List<EvolvesToEntry>?
)

data class SpeciesEntry(
    val name: String,
    val url: String
)