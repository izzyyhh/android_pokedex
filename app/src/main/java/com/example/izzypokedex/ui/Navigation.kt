package com.example.izzypokedex.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi

val LocalNavController = compositionLocalOf<NavController> {error("No active NavController")  }

@ExperimentalPagingApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = "list_screen" ) {
            composable("list_screen") { PokemonListScreen() }
            composable("detail_screen/{pokemonId}") { backStackEntry ->
                PokemonDetailScreen(backStackEntry.arguments?.getString("pokemonId")?.toInt() ?: 25)
            }
        }
    }


}