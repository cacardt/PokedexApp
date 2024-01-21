package com.example.pokedex.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.example.pokedex.network.Evolutions

@Serializable
data class Pokemon (
    val description: String,

    @SerialName("evolutions")
    val evolutionsData: Evolutions,

    val id: Int,

    @SerialName("image_url")
    val imageUrl: String,

    val name: String,

    val type: List<String>
)