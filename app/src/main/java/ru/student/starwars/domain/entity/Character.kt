package ru.student.starwars.domain.entity

data class Character(
    val id: String = "",
    val name: String = "Name",
    val height: Int = 175,
    val mass: Int = 70,
    val gender: Gender = Gender.MALE,
    val skinColor: String = "white",
    val eyeColor: String = "brown",
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val starshipsCount: Int = 0
) : Searchable {
    override fun getSearchableText(): String {
        return name
    }
}

