package com.example.izzypokedex.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.izzypokedex.Pokemon

val LocalNavController = compositionLocalOf<NavController> {error("No active NavController")  }

@ExperimentalPagingApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = "list_screen" ) {
            composable("list_screen") {
                val listVm: PokemonViewModel = hiltViewModel()
                val lazyListState = rememberLazyListState()
                val pokemonItems: LazyPagingItems<Pokemon> = listVm.pokemon.collectAsLazyPagingItems()
                PokemonListScreen(pokemonItems = pokemonItems, listState = lazyListState)
            }
            composable("detail_screen/{pokemonId}") { backStackEntry ->
                PokemonDetailScreen(backStackEntry.arguments?.getString("pokemonId")?.toInt() ?: 25)
            }
        }
    }
}