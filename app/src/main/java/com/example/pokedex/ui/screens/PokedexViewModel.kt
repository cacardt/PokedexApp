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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokedex.PokedexApplication
import com.example.pokedex.data.PokemonPhotosRepository
import com.example.pokedex.network.Pokemon
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface PokemonUiState {
    data class Success(val photos: List<Pokemon>) : PokemonUiState
    object Error : PokemonUiState
    object Loading : PokemonUiState
}

class PokedexViewModel(private val pokemonsRepository: PokemonPhotosRepository) : ViewModel() {

    var pokeUiState: PokemonUiState by mutableStateOf(PokemonUiState.Loading)
        private set

    private var allPokemons: List<Pokemon> = emptyList()

    var searchQuery by mutableStateOf("")

    init {
        getPokemonsPhotos()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        filterPokemons()
    }

    private fun filterPokemons() {
        val filteredList = if (searchQuery.isBlank()) {
            allPokemons
        } else {
            allPokemons.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        pokeUiState = PokemonUiState.Success(filteredList)
    }

    fun getPokemonsPhotos() {
        pokeUiState = PokemonUiState.Loading
        viewModelScope.launch {
            pokeUiState = try {
                allPokemons  = pokemonsRepository.getPokemons()
                PokemonUiState.Success(allPokemons)
            } catch (e: IOException) {
                PokemonUiState.Error
            }
        }
    }

    fun findPokemonById(pokemonId: Int): Pokemon? {
        return allPokemons.firstOrNull { it.id == pokemonId }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokedexApplication)
                val pokemonRepository = application.container.pokemonRepository
                PokedexViewModel(pokemonsRepository = pokemonRepository)
            }
        }
    }
}
