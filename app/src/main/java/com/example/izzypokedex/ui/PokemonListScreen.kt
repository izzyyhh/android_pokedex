package com.example.izzypokedex.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.color
import com.example.izzypokedex.ui.shared.PokemonImage
import com.example.izzypokedex.ui.shared.PokemonName
import com.example.izzypokedex.ui.shared.PokemonNumber
import com.example.izzypokedex.ui.shared.PokemonTypeBadges
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@ExperimentalPagingApi
@Composable
fun PokemonListScreen(pokemonItems: LazyPagingItems<Pokemon>, listState: LazyListState){
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            text = "This Pokédex contains detailed stats for every creature from the Pokémon games.",
            style = MaterialTheme.typography.body1.copy(
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        when(pokemonItems.loadState.refresh) {
            LoadState.Loading -> {
                LinearProgressIndicator(
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            else -> {
                PokemonLazyList(listState = listState, pokemonItems = pokemonItems )
            }
        }
    }
}

@Composable
fun PokemonLazyList(listState: LazyListState, pokemonItems: LazyPagingItems<Pokemon> ) {
    val navController = LocalNavController.current

    LazyColumn(
        state = listState
    ) {
        items(pokemonItems) {
            if(it != null ) {
                PokemonCard(pokemon = it, onClick = {navController.navigate("detail_screen/${it.id}")})
            }
        }

        if(pokemonItems.loadState.append is LoadState.Loading) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(strokeWidth = 4.dp, color = MaterialTheme.colors.onSurface)
                }
            }
        } else {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Something unexepected happened")
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = pokemon.color()))
                    .fillMaxSize()
                    .clickable { onClick() }
            ) {
                Row {
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    PokemonImage(image = pokemon.frontOfficialDefault)
                    Box {
                        Column(
                            modifier = Modifier
                                .padding(PaddingValues(start = 8.dp))
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Spacer(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            PokemonName(name = pokemon.name)
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                            PokemonTypeBadges(pokemonTypes = pokemon.types)
                        }
                        Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp)
                        ) {
                            PokemonNumber(pokemonId = pokemon.id, alpha = 0.4f)
                        }
                    }
                }
            }
        }
    }
}

/*@Composable
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
}*/

