package com.example.izzypokedex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.color
import com.example.izzypokedex.ui.shared.PokemonImage
import com.example.izzypokedex.ui.shared.PokemonName
import com.example.izzypokedex.ui.shared.PokemonNumber
import com.example.izzypokedex.ui.shared.PokemonTypeBadges
import com.example.izzypokedex.ui.theme.Shapes
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
    is DataState.Error -> Text(text= dataState.exception.toString())
    is DataState.Success<Pokemon> -> PokemonDetailContent(pokemon = dataState.data)
}

@Composable
fun PokemonDetailContent(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = pokemon.color()))
    ) {
        PokemonDetailContent(name = pokemon.name, id = pokemon.id, image = pokemon.frontOfficialDefault, pokemon.types)
    }
}

@Composable
fun PokemonDetailContent(name: String, id: Int, image: String, types: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.58f)
                .fillMaxWidth()
                .align(BottomCenter)
                .clip(RoundedCornerShape(topStartPercent = 8, topEndPercent = 8)),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
            ) {

            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                PokemonName(name = name, fontSize = 40)
                PokemonNumber(pokemonId = id, fontSize = 24, alpha = 1f)
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            PokemonTypeBadges(
                pokemonTypes = types,
                fontSize = MaterialTheme.typography.body1.fontSize,
                alpha = 1f,
                horizontalPadding = 28,
                verticalPadding = 6
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Bottom
            ) {
                PokemonImage(image = image)
            }
        }
    }
}




/*
Text(text = "detail")
Row {
    Text(text = pokemon.name)
    Text(text = pokemon.id.toString())
    Text(text = pokemon.species.color)
}
Text(text = pokemon.species.text)
Text(text = pokemon.height.toString())
Text(text = pokemon.weight.toString())
Text(text = pokemon.stats.hp.toString())
Text(text = pokemon.stats.attack.toString())
Text(text = pokemon.stats.defense.toString())
Text(text = pokemon.stats.specialAttack.toString())
Text(text = pokemon.stats.specialDefense.toString())
Text(text = pokemon.stats.speed.toString())
pokemon.evolution.forEach {
    Text(text = it.toString())
}*/
