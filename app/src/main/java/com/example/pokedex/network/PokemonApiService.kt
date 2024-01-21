package com.example.pokedex.network

import retrofit2.http.GET

interface PokemonApiService {
    @GET("main/data2.json")
    suspend fun getPokemons(): List<Pokemon>
}
