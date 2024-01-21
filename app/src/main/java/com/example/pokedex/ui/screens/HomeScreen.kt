/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.Screen
import com.example.pokedex.network.Pokemon
import com.example.pokedex.ui.theme.PokedexTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    pokeUIState: PokemonUiState,
    pokeViewModel: PokedexViewModel,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
)  {
    val scrollState = rememberScrollState()

    Column {
        StickyHeader(pokeViewModel)
        when (pokeUIState) {
            is PokemonUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            is PokemonUiState.Success -> PhotosGridScreen(
                pokeUIState.photos,
                onPokemonClick = onPokemonClick,
                onRefresh = { pokeViewModel.getPokemonsPhotos() },
                modifier = modifier.fillMaxWidth()
            )

            is PokemonUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
        }
    }
}



@Composable
fun StickyHeader(viewModel: PokedexViewModel) {
    Spacer(Modifier.height(4.dp))
    Column {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                onSearch = {
                    query -> viewModel.updateSearchQuery(query)
                }
            )
            IconButton(onClick = viewModel::getPokemonsPhotos) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        }
    }
}



/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(photos: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = photos)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    PokedexTheme {
        ResultScreen(stringResource(R.string.placeholder_result))
    }
}


@Composable
fun PokemonCard(photo: Pokemon, onPokemonClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = colorType(photo.type.firstOrNull()?.lowercase())

    Card(
        modifier = modifier
            .padding(1.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onPokemonClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = photo.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    photo.type.forEach { type ->
                        TypeLabel(type = type)
                        Spacer(Modifier.height(4.dp))
                    }
                }
                Spacer(Modifier.width(20.dp))
                PokemonImage(imageUrl = photo.imageUrl, size = 95.dp, modifier = Modifier.scale(1.5f))
            }
        }
    }
}

@Composable
fun PokemonImage(imageUrl: String?, size: Dp, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Pokémon Image",
        contentScale = ContentScale.FillBounds,
        modifier = modifier.size(size)
    )
}

@Composable
fun TypeLabel(type: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 1.dp)
        ) {
            Image(
                painter = painterResource(id = typeIconResource(type)),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = type,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun colorType(type: String?): Color {
    return when (type) {
        "fire" -> Color(0xFFE72324)
        "water" -> Color(0xFF2481EF)
        "bug" -> Color(0xFF92A212)
        "flying" -> Color(0xFF82BAEF)
        "poison" -> Color(0xFF923FCC)
        "grass" -> Color(0xFF3DA224)
        "normal" -> Color(0xFFA0A2A0)
        "electric" -> Color(0xFFFAC100)
        "psychic" -> Color(0xFFEF3F7A)
        "ground" -> Color(0xFF92501B)
        "dark" -> Color(0xFF4F3F3D)
        "ghost" -> Color(0xFF703F70)
        "rock" -> Color(0xFFB0AA82)
        "ice" -> Color(0xFF3DD9FF)
        "fairy" -> Color(0xFFEF70EF)
        "dragon" -> Color(0xFF4F60E2)
        "fighting" -> Color(0xFFFF8100)
        "steel" -> Color(0xFF60A2B9)
        else -> Color(0xFF78C850)
    }
}


@Composable
fun typeIconResource(type: String): Int {
    return when (type.lowercase()) {
        "fire" -> R.drawable.fire_icon_swsh
        "water" -> R.drawable.water_icon_swsh
        "poison" -> R.drawable.poison_icon_swsh
        "bug" -> R.drawable.bug_icon_swsh
        "flying" -> R.drawable._20px_flying_icon_swsh
        "normal" -> R.drawable.normal_icon_swsh
        "grass" -> R.drawable.grass_icon_swsh
        "fighting" -> R.drawable._20px_fighting_icon_swsh
        "fairy" -> R.drawable._20px_fairy_icon_swsh
        "ice" -> R.drawable._20px_ice_icon_swsh
        "ghost" -> R.drawable._20px_ghost_icon_swsh
        "dark" -> R.drawable._20px_dark_icon_swsh
        "psychic" -> R.drawable._20px_psychic_icon_swsh
        "rock" -> R.drawable._20px_rock_icon_swsh
        "steel" -> R.drawable._20px_steel_icon_swsh
        "ground" -> R.drawable._20px_ground_icon_swsh
        "electric" -> R.drawable._20px_electric_icon_swsh
        "dragon" -> R.drawable._20px_dragon_icon_swsh

        else -> R.drawable.ic_broken_image
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosGridScreen(
    photos: List<Pokemon>,
    onPokemonClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }
        }
    }

    val pullRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = { }
    ) { innerPadding ->
        SwipeRefresh(
            state = pullRefreshState,
            onRefresh = { onRefresh() }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = photos, key = { photo -> photo.id }) { photo ->
                    PokemonCard(photo = photo, onPokemonClick = { onPokemonClick(photo.id) })
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {

    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search...") },
        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchText)
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        )
    )
}





@Composable
fun PokemonDetailScreen(pokemonId: Int, viewModel: PokedexViewModel, navController: NavController) {
    val pokemon = viewModel.findPokemonById(pokemonId)
    val background = colorType(pokemon?.type?.firstOrNull()?.lowercase())
    val evolutions = (pokemon?.evolutionsData?.before ?: listOf()) + (pokemon?.evolutionsData?.after ?: listOf())

    Scaffold(
        topBar = {
            DetailTopBar(pokemon?.name ?: "Pokémon", navController, background)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(background)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ){
                    pokemon?.type?.forEach { type ->
                        TypeLabel(type = type)
                        Spacer(Modifier.width(4.dp))
                    }
                }
                Box(
                    modifier = Modifier
                ) {
                    PokemonImage(imageUrl = pokemon?.imageUrl, size = 300.dp, modifier = Modifier)
                }
                Surface(
                    modifier = Modifier
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                    color = Color.White
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Spacer(Modifier.weight(0.1f))
                        Text(
                            text = "Description :",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(15.dp)
                        )
                        Text(
                            text = pokemon?.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "Évolutions :",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(15.dp)
                        )
                        EvolutionSection(
                            viewModel = viewModel,
                            evolutions = evolutions,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EvolutionSection(viewModel: PokedexViewModel, evolutions: List<Int>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(evolutions) { evolutionId ->
            val evolutionPokemon = viewModel.findPokemonById(evolutionId)
            evolutionPokemon?.let { pokemon ->
                val typeColor = colorType(pokemon.type.firstOrNull()?.lowercase())
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(80.dp)
                        .clickable {
                            navController.navigate(
                                Screen.PokemonDetailScreen.createRoute(pokemon.id)
                            )
                        }
                        .shadow(8.dp, CircleShape),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Pokémon Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorType(pokemon.type[0]))
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailTopBar(title: String, navController: NavController, background: Color) {
    TopAppBar(
        title = { Text(text = title, color = MaterialTheme.colorScheme.onPrimary) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("pokemon_list_screen") }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Accueil",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = background,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}