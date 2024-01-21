package com.example.pokedex.fake

import com.example.pokedex.data.PokemonPhotosRepository
import com.example.pokedex.network.Pokemon

class FakeNetworkPokemonRepository : PokemonPhotosRepository {
    override suspend fun getPokemons(): List<Pokemon> {
        return FakeDataSource.photosList
    }
}