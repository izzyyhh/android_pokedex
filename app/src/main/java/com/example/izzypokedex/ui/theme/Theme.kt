package com.example.izzypokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PokedexRed,
    primaryVariant = Purple700,
    secondary = Teal200,
    surface = DarkBackground,
    onSurface = PokedexRed,
    background = Color.Black,
    onBackground = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White.copy(0.2f)
)

private val LightColorPalette = lightColors(
    primary = PokedexRed,
    onPrimary = Color.White,
    primaryVariant = Purple700,
    secondary = Color.DarkGray,
    onSurface = PokedexRed,
    background = Color.White,
    onBackground = TextGray,
    surface = SurfaceGray,
    onSecondary = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun IzzyPokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}