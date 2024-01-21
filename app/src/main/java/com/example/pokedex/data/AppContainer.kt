package com.example.pokedex.data

import com.example.pokedex.network.PokemonApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val pokemonRepository : PokemonPhotosRepository
}

class DefaultAppContainer : AppContainer{

    private val BASE_URL = "https://raw.githubusercontent.com/cacardt/IoT_Pokedex/"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService : PokemonApiService by lazy {
        retrofit.create(PokemonApiService::class.java)
    }
    override val pokemonRepository: PokemonPhotosRepository by lazy {
        NetworkPokemonRepository(retrofitService)
    }
}