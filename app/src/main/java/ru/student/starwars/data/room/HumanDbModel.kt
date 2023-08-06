package ru.student.starwars.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_people")
data class HumanDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val height: Int,
    val mass: Int,
    val gender: String,
    val skinColor: String,
    val eyeColor: String,
    val imageUrl: String,
    val name: String,
    val isFavorite: Boolean
)