package com.example.pokedex.fake

import com.example.pokedex.network.Evolutions
import com.example.pokedex.network.Pokemon

object FakeDataSource {
    const val idOne = 1
    const val idTwo = 2
    const val imgOne = "url.1"
    const val imgTwo = "url.2"
    val photosList = listOf(
        Pokemon(
            id = idOne,
            imageUrl = imgOne,
            name = "nameOne",
            description = "descriptionOne",
            type = listOf("typeOneOne"),
            evolutionsData = Evolutions(before = listOf(), after = listOf())
        ),
        Pokemon(
            id = idTwo,
            imageUrl = imgTwo,
            name = "nameTwo",
            description = "descriptionTwo",
            type = listOf("typeTwo"),
            evolutionsData = Evolutions(before = listOf(), after = listOf())
        )
    )
}