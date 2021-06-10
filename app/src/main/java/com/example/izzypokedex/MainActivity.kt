package com.example.izzypokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.izzypokedex.ui.Navigation
import com.example.izzypokedex.ui.theme.IzzyPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("izzydesu", "https://pokeapi.co/api/v2/pokemon/14/")
        val str = "https://pokeapi.co/api/v2/pokemon/14/".split("/")

        Log.i("izzydesu", str[str.size -2])

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