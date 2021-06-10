package com.example.izzypokedex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.util.DataState

@Composable
fun PokemonDetailScreen(pokeId: Int) {
    val pokemonDetailViewModel: PokemonDetailViewModel = hiltViewModel()
    val dataState = pokemonDetailViewModel.dataState.observeAsState(DataState.Loading).value

    LaunchedEffect(key1 = "test" ) {
        pokemonDetailViewModel.setEvent(PokemonDetailEvent.GetPokemonEvent(pokeId))
    }

    PokemonDetailScreen(dataState = dataState)
}

@Composable
fun PokemonDetailScreen(dataState: DataState<Pokemon>) = when(dataState) {
    is DataState.Loading -> CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    )
    is DataState.Error -> Text(text= "error")
    is DataState.Success<Pokemon> -> PokemonDetailContent(pokemon = dataState.data)
}

@Composable
fun PokemonDetailContent(pokemon: Pokemon) {
    Column {
        Text(text = "detail")
        Row {
            Text(text = pokemon.name)
            Text(text = pokemon.id.toString())
        }
    }
}