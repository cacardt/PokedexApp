package com.example.pokedex.fake

import com.example.pokedex.network.Pokemon
import com.example.pokedex.network.PokemonApiService

class FakePokemonApiService : PokemonApiService {
    override suspend fun getPokemons(): List<Pokemon>(): List<Pokemon> {
        return FakeDataSource.photosList
    }
}