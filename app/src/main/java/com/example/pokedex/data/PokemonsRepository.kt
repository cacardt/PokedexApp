package com.example.pokedex.data

import com.example.pokedex.network.Pokemon
import com.example.pokedex.network.PokemonApiService

interface PokemonPhotosRepository {
    suspend fun getPokemons(): List<Pokemon>
}

class NetworkPokemonRepository(
    private val pokemonApiService: PokemonApiService
) : PokemonPhotosRepository {
    override suspend fun getPokemons(): List<Pokemon> = pokemonApiService.getPokemons()
}