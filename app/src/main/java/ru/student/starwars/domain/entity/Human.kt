package ru.student.starwars.domain.entity

data class Human(
    val id: String = "",
    val name: String = "Name",
    val height: Int = 175,
    val mass: Int = 70,
    val gender: String = "male",
    val skinColor: String = "white",
    val eyeColor: String = "brown",
    val imageUrl: String = "",
    val isFavorite: Boolean
)
