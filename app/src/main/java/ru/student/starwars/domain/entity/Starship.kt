package ru.student.starwars.domain.entity

data class Starship(
    val id: String = "",
    val name: String = "",
    val model: String = "",
    val manufacturer: String = "",
    val passengers: String = "",
    val isFavorite: Boolean = false
) : Searchable {
    override fun getSearchableText(): String {
        return name
    }
}