package com.example.izzypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.izzypokedex.ui.Navigation
import com.example.izzypokedex.ui.PokemonStateEvent
import com.example.izzypokedex.ui.PokemonViewModel
import com.example.izzypokedex.ui.theme.IzzyPokedexTheme
import com.example.izzypokedex.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IzzyPokedexTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val pokemonViewModel: PokemonViewModel = hiltViewModel()

    LaunchedEffect(key1 = "ok1"){
        pokemonViewModel.setStateEvent(PokemonStateEvent.GetPokemonEvent)
    }


    when(val data = pokemonViewModel.dataState.observeAsState().value) {
        is DataState.Loading -> {
            Text(text = data.toString())
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            )
        }
        is DataState.Success<List<Pokemon>> -> {
            val pokes = data.data
            Row() {
                Text(text = pokes.size.toString() + " $name")
            }
        }
        is DataState.Error -> {
            Text(text = "alles klar")
        }
        else -> {
            Text(text = "else")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IzzyPokedexTheme {
        Greeting("Izzy")
    }
}