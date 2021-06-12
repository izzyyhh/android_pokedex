package com.example.izzypokedex.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun PokemonNumber(pokemonId: Int, fontSize: Int = 36, alpha: Float = 1f) {
    val numText = when(pokemonId.toString().length) {
        1 -> "#00$pokemonId"
        2 -> "#0$pokemonId"
        else -> "#$pokemonId"
    }

    Text(
        text = numText,
        color = Color.White.copy(alpha = alpha),
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize.sp,
        lineHeight = 0.sp
    )
}

@Composable
fun PokemonTypeBadges(pokemonTypes: List<String>, fontSize: TextUnit = MaterialTheme.typography.body2.fontSize, verticalPadding: Int = 4, horizontalPadding: Int = 16, alpha: Float = 0.8f) {
    Row{
        pokemonTypes.map { type ->
            Text(
                text = type.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colors.onPrimary.copy(alpha = alpha),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp),
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Composable
fun PokemonName(name: String, fontSize: Int = 28, fontWeight: FontWeight = FontWeight.Bold) {
    Text(
        text = name.replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onPrimary,
        fontWeight = fontWeight,
        fontSize = fontSize.sp
    )
}

@Composable
fun PokemonImage(image: String) {
    Box{
        val painter = rememberCoilPainter(request = image)
        Image(
            painter = painter,
            contentDescription = "pokemon"
        )
        if(painter.loadState is ImageLoadState.Loading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}