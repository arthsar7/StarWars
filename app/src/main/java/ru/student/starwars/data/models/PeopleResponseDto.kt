package ru.student.starwars.data.models

import com.google.gson.annotations.SerializedName

data class PeopleResponseDto(
    @SerializedName("results")val people: List<HumanDto>,
    @SerializedName("next") val nextDataUrl: String
)
