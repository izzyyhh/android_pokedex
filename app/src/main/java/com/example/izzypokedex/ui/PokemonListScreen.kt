package com.example.izzypokedex.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.izzypokedex.Pokemon
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@ExperimentalPagingApi
@Composable
fun PokemonListScreen(pokemonItems: LazyPagingItems<Pokemon>, listState: LazyListState){
    val navController = LocalNavController.current

    LazyColumn(
        state = listState
    ) {
        items(pokemonItems) {
            if(it != null ) {
                PokemonListCard(name = it.name, image = it.frontOfficialDefault, it.id, onClick = {navController.navigate("detail_screen/${it.id}")})
            }
        }

        if(pokemonItems.loadState.append is LoadState.Loading) {
            item {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PokemonListCard(name: String, image: String, num: Int, onClick: () -> Unit ) {
    Card(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .background(MaterialTheme.colors.secondary)
        ){
            Text(num.toString())
            Text(text = name.uppercase())
            Box{
                val painter =rememberCoilPainter(request = image)
                Image(
                    painter = painter,
                    contentDescription = name
                )
                if(painter.loadState is ImageLoadState.Loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
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

