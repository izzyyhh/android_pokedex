package com.example.izzypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.ExperimentalPagingApi
import com.example.izzypokedex.ui.Navigation
import com.example.izzypokedex.ui.theme.IzzyPokedexTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            IzzyPokedexTheme {
                systemUiController.setSystemBarsColor(
                    color = if(isSystemInDarkTheme()) Color.Black else Color.White,
                    darkIcons = !isSystemInDarkTheme()
                )

                Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}