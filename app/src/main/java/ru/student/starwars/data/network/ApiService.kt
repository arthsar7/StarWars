package ru.student.starwars.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.student.starwars.data.models.HumanDto
import ru.student.starwars.data.models.PeopleResponseDto

interface ApiService {
    @GET("people")
    suspend fun getPeople(): PeopleResponseDto

    @GET("people/{id}")
    suspend fun getPeopleById(@Path("id") id: String): HumanDto

}
