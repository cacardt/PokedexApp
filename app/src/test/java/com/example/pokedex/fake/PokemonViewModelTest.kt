package com.example.pokedex.fake

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.pokedex.fake.rules.TestDispatcherRule
import com.example.pokedex.network.Pokemon
import com.example.pokedex.ui.screens.PokedexViewModel
import com.example.pokedex.ui.screens.PokemonCard
import com.example.pokedex.ui.screens.PokemonUiState
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

class PokemonViewModelTest {
    // Mock de vos dépendances, par exemple un Repository
    private lateinit var mockRepository: PokemonRepository
    private lateinit var viewModel: PokedexViewModel

    @Before
    fun setup() {
        mockRepository = mock(PokemonRepository::class.java)
        viewModel = PokedexViewModel(mockRepository)
    }
    @Test
    fun getPokemonsPhotos_updatesUiState() {
        // Arrange
        val expectedPhotos = listOf<Pokemon>() // Remplacer par des données de test
        `when`(mockRepository.getPokemonsPhotos()).thenReturn(expectedPhotos)

        // Act
        viewModel.getPokemonsPhotos()

        // Assert
        assertEquals(expectedPhotos, viewModel.pokeUIState.value)
    }
}
@OptIn(ExperimentalComposeUiApi::class)
class PokemonCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonCard_displaysCorrectData() {
        // Arrange
        val pokemon = Pokemon(/* données de test */)

        // Act
        composeTestRule.setContent {
            PokemonCard(photo = pokemon, onPokemonClick = { /* ... */ })
        }

        // Assert
        composeTestRule.onNodeWithText(pokemon.name).assertIsDisplayed()
    }
}

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigateToPokemonDetailScreen_navigatesSuccessfully() {
        // Arrange
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            PokedexTheme {
                NavHost(navController, startDestination = "home_screen") {
                    composable("home_screen") { HomeScreen(/* ... */) }
                    composable("pokemon_detail_screen/{pokemonId}") { backStackEntry ->
                        PokemonDetailScreen(backStackEntry.arguments?.getInt("pokemonId"), /* ... */)
                    }
                }
            }
        }

        // Act
        navController.navigate("pokemon_detail_screen/1")

        // Assert
        assertEquals(navController.currentDestination?.route, "pokemon_detail_screen/{pokemonId}")
    }
}

