package ru.student.starwars.data.models

import com.google.gson.annotations.SerializedName

data class StarshipsResponseDto(
    @SerializedName("results") val starships: List<StarshipDto>,
    @SerializedName("next") val nextDataUrl: String
)
