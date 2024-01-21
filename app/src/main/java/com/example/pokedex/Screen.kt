package com.example.pokedex

sealed class Screen(val route: String) {
    object PokemonListScreen : Screen("pokemon_list_screen")
    object PokemonDetailScreen : Screen("pokemon_detail_screen/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail_screen/$pokemonId"
    }
}