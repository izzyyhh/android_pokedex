package com.example.izzypokedex.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.util.DataState

@Composable
fun PokemonListScreen(){
    val navController = LocalNavController.current

    val viewModel: PokemonViewModel = hiltViewModel()
    val state = viewModel.dataState.observeAsState(DataState.Loading).value

    LaunchedEffect(key1 = "get_pokemon_list_event") {
        viewModel.setStateEvent(PokemonStateEvent.GetPokemonEvent)
    }

    PokemonListScreen(state)
}

@Composable
fun PokemonListScreen(dataState: DataState<List<Pokemon>>) {
    when(dataState) {
        is DataState.Loading -> CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        )
        is DataState.Error -> Text(text= "error")
        is DataState.Success<List<Pokemon>> -> PokemonListContent(pokemonList = dataState.data)
    }
}

@Composable
fun PokemonListContent(pokemonList: List<Pokemon>) {
    val navController = LocalNavController.current
    LazyColumn() {
        items(pokemonList) { pokemon ->
            Text(pokemon.name)
        }

        item {
            Button(onClick = { navController.navigate("detail_screen") }) {
                Text(text = "navigate")
            }
        }
    }
}

