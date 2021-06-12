package com.example.izzypokedex

import androidx.compose.ui.graphics.Color
import com.example.izzypokedex.ui.theme.*

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
    val evolution: List<Pokemon> = emptyList(),
    val happiness: Int = 100,
    val captureRate: Int = 0
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

fun Pokemon.color(): Int = when(types[0]) {
        "grass", "bug" -> R.color.pokemon_green
        "ground", "rock" -> R.color.pokemon_brown
        "water", "fighting", "normal" -> R.color.pokemon_blue
        "poison", "ghost" -> R.color.pokemon_purple
        "electric", "psychic" -> R.color.pokemon_yellow
        "fire" -> R.color.pokemon_red
        "dark" -> R.color.pokemon_black
        else -> R.color.pokemon_black
    }
