package com.example.izzypokedex.ui

import android.view.Surface
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navArgument
import com.example.izzypokedex.Pokemon
import com.example.izzypokedex.color
import com.example.izzypokedex.ui.shared.PokemonImage
import com.example.izzypokedex.ui.shared.PokemonName
import com.example.izzypokedex.ui.shared.PokemonNumber
import com.example.izzypokedex.ui.shared.PokemonTypeBadges
import com.example.izzypokedex.ui.theme.Shapes
import com.example.izzypokedex.util.DataState
import kotlinx.coroutines.delay
import java.lang.Exception

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
        PokemonDetailWrapper(pokemon = pokemon)
    }
}

@Composable
fun PokemonDetailWrapper(pokemon: Pokemon) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                PokemonDetail(pokemon = pokemon)
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
                PokemonName(name = pokemon.name, fontSize = 40)
                PokemonNumber(pokemonId = pokemon.id, fontSize = 24, alpha = 1f)
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            PokemonTypeBadges(
                pokemonTypes = pokemon.types,
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
                PokemonImage(image = pokemon.frontOfficialDefault)
            }
        }
    }
}

@Composable
fun PokemonDetail(pokemon: Pokemon) {

    var section: Section by remember{ mutableStateOf(Section.About)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Section.values().map {
                Button(
                    onClick = { section = it },
                    modifier = Modifier
                        .width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = colorResource(id = pokemon.color())),
                    elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    if(it == section) {
                        Text(text = it.heading, color = colorResource(id = pokemon.color()))
                    } else {
                        Text(text = it.heading, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = section.heading,
            color = colorResource(id = pokemon.color()),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )

        when(section) {
            Section.About -> PokemonAbout(pokemon)
            Section.Stats -> PokemonStats(pokemon)
            Section.Evolution -> PokemonEvolution(pokemon)
        }
    }
}

private enum class Section(val heading: String) {
    About("About"),
    Stats("Stats"),
    Evolution("Evolution")
}

@Composable
fun PokemonAbout(pokemon: Pokemon) {
    Column() {
        Text(text = pokemon.species.text.replace("\n", " "))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Column {
                Text("Height", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Weight", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Happiness", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Capture Rate", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
            }

            Column {
                Text(text = (pokemon.height * 10).toString() + "cm", fontWeight = FontWeight.Bold)
                Text(text = (pokemon.weight / 10.0).toString() + "kg", fontWeight = FontWeight.Bold)
                Text(text = pokemon.happiness.toString(), fontWeight = FontWeight.Bold)
                Text(text = pokemon.captureRate.toString(), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PokemonEvolution(pokemon: Pokemon) {
    val navController = LocalNavController.current

    Text(text = "This pokemon has these evolution stages.")
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    
    LazyColumn {
        pokemon.evolution.filter { it.id != pokemon.id }.forEach { evoPoke ->
            item {
                PokemonCard(pokemon = evoPoke) {
                    navController.navigate("detail_screen/${evoPoke.id}")
                }
            }
        }
    }
}

@Composable
fun PokemonStats(pokemon: Pokemon) {
    // I didn't come up with a better idea to animate all floats the same way in a loop ...
    // this is not DRY!
    // define stat objects
    // TODO("REFACTOR, into list of stat object")

    var hpProgress by remember {
        mutableStateOf(0f)
    }

    var attProgress by remember {
        mutableStateOf(0f)
    }

    var defProgress by remember {
        mutableStateOf(0f)
    }

    var spAttProgress by remember {
        mutableStateOf(0f)
    }

    var spDefProgress by remember {
        mutableStateOf(0f)
    }

    var speedProgress by remember {
        mutableStateOf(0f)
    }

    var totalProgress by remember {
        mutableStateOf(0f)
    }

    val hpTransition by animateFloatAsState(
        targetValue = hpProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val attTransition by animateFloatAsState(
        targetValue = attProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val defTransition by animateFloatAsState(
        targetValue = defProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val spAttTransition by animateFloatAsState(
        targetValue = spAttProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val spDefTransition by animateFloatAsState(
        targetValue = spDefProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val speedTransition by animateFloatAsState(
        targetValue = speedProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val totalTransition by animateFloatAsState(
        targetValue = totalProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val pokemonTotal = pokemon.stats.hp + pokemon.stats.attack + pokemon.stats.defense + pokemon.stats.specialAttack + pokemon.stats.specialDefense + pokemon.stats.speed

    LaunchedEffect(key1 = "animation_stats") {
        delay(50)
        // diagram progress
        hpProgress = if(pokemon.stats.hp / 100f > 1f) 1f else pokemon.stats.hp / 100f
        attProgress = if(pokemon.stats.attack / 100f > 1f) 1f else pokemon.stats.attack / 100f
        defProgress = if(pokemon.stats.defense / 100f > 1f) 1f else pokemon.stats.defense / 100f
        spAttProgress = if(pokemon.stats.specialAttack / 100f > 1f) 1f else pokemon.stats.specialAttack / 100f
        spDefProgress = if(pokemon.stats.specialDefense / 100f > 1f) 1f else pokemon.stats.specialDefense / 100f
        speedProgress = if(pokemon.stats.speed / 100f > 1f) 1f else pokemon.stats.speed / 100f
        totalProgress = if(pokemonTotal / 600f > 1f) 1f else pokemonTotal / 600f
    }

    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column() {
                Text("Hp", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Attack", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Defense", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Sp. Atk", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Sp. Def", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Speed", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
                Text("Total", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondary)
            }

            Column() {
                StatBar(indicatorProgress = hpTransition, text = pokemon.stats.hp.toString())
                StatBar(indicatorProgress = attTransition, text = pokemon.stats.attack.toString())
                StatBar(indicatorProgress = defTransition, text = pokemon.stats.defense.toString())
                StatBar(indicatorProgress = spAttTransition, text = pokemon.stats.specialAttack.toString())
                StatBar(indicatorProgress = spDefTransition, text = pokemon.stats.specialDefense.toString())
                StatBar(indicatorProgress = speedTransition, text = pokemon.stats.speed.toString())
                StatBar(indicatorProgress = totalTransition, text = pokemonTotal.toString())
            }
        }
    }
}

@Composable
fun StatBar(indicatorProgress: Float, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, modifier = Modifier.width(36.dp), textAlign = TextAlign.End)

        LinearProgressIndicator(
            color = MaterialTheme.colors.primary,
            progress = indicatorProgress
        )
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
