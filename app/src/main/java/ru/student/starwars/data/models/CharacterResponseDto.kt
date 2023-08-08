package ru.student.starwars.data.models

import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("results")val characters: List<CharacterDto>,
    @SerializedName("next") val nextDataUrl: String
)
