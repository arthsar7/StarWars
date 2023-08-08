package ru.student.starwars.data.models

import com.google.gson.annotations.SerializedName

data class StarshipDto (
    @SerializedName("url")val url: String,
    @SerializedName("name")val name: String,
    @SerializedName("model")val model: String,
    @SerializedName("manufacturer")val manufacturer: String,
    @SerializedName("passengers")val passengers: String
)