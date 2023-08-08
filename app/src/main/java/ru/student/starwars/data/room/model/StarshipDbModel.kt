package ru.student.starwars.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_starships")
data class StarshipDbModel (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val model: String,
    val manufacturer: String,
    val passengers: String,
    val isFavorite: Boolean
)
