package ru.student.starwars.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_characters")
data class CharacterDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val height: Int,
    val mass: Int,
    val gender: String,
    val skinColor: String,
    val eyeColor: String,
    val imageUrl: String,
    val name: String,
    val isFavorite: Boolean,
    val starshipsCount: Int
)