package com.example.izzypokedex.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

@ExperimentalPagingApi
@Composable
fun PokemonListScreen(pokemonItems: LazyPagingItems<Pokemon>, listState: LazyListState){
    var query: String by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        SearchBar(query = query, setQuery = {query = it})
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
                PokemonList(listState = listState, pokemonItems = pokemonItems, query = query)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, setQuery: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(percent = 30)
    ) {

        TextField(
            value = query,
            onValueChange = { setQuery(it) },
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            placeholder = { Text(text = "Search for a Pokemon") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Search, "Poke Search", modifier = Modifier
                .clickable { focusManager.clearFocus() }
                .padding(0.dp))},
            trailingIcon = { if (query.isNotEmpty()) Icon(Icons.Filled.Clear, "Remove search", modifier = Modifier.clickable {
                setQuery("")
                focusManager.clearFocus() })
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                textColor = MaterialTheme.colors.onBackground,
                placeholderColor = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                leadingIconColor = MaterialTheme.colors.onBackground,
                trailingIconColor = MaterialTheme.colors.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}


@Composable
fun PokemonList(listState: LazyListState, pokemonItems: LazyPagingItems<Pokemon>, query: String) {
    val navController = LocalNavController.current

    LazyColumn(
        state = listState
    ) {
        if(query.isEmpty()) {
            // if no query show all pokes
            items(pokemonItems) {
                if(it != null) {
                    PokemonCard(pokemon = it, onClick = {navController.navigate("detail_screen/${it.id}")})
                }
            }

            if(pokemonItems.loadState.append is LoadState.Loading) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CircularProgressIndicator(strokeWidth = 4.dp, color = MaterialTheme.colors.onSurface)
                    }
                }
            }

        } else {
            // if query then show queried pokes
            val pokeSearchResult = pokemonItems.snapshot().filter { pokemon -> pokemon != null && pokemon.name.contains(query.trim().lowercase()) }

            items(pokeSearchResult) {
                if(it != null) {
                PokemonCard(pokemon = it) {
                    navController.navigate("detail_screen/${it.id}")}
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
                    .background(color = pokemon.color())
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
                            PokemonName(name = pokemon.name, fontSize = 24)
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                            PokemonTypeBadges(pokemonTypes = pokemon.types, verticalPadding = 2, horizontalPadding = 12)
                        }
                        Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp)
                        ) {
                            PokemonNumber(pokemonId = pokemon.id, alpha = 0.4f, fontSize = 32)
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

