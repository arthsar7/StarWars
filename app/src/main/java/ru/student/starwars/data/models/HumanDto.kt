package ru.student.starwars.data.models

import com.google.gson.annotations.SerializedName

data class HumanDto(
    @SerializedName("name")val name: String,
    @SerializedName("height")val height: Int,
    @SerializedName("mass")val mass: Int,
    @SerializedName("gender")val gender: String,
    @SerializedName("skin_color")val skinColor: String,
    @SerializedName("eye_color")val eyeColor: String,
    @SerializedName("url") val url: String
)
