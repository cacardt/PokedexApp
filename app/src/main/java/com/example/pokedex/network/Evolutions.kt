package com.example.pokedex.network

import kotlinx.serialization.Serializable


@Serializable
data class Evolutions (
    val after: List<Int>,
    val before: List<Int>
)