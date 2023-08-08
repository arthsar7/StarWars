package ru.student.starwars.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.student.starwars.data.models.HumanDto
import ru.student.starwars.data.models.PeopleResponseDto
import ru.student.starwars.data.models.StarshipDto
import ru.student.starwars.data.models.StarshipsResponseDto

interface ApiService {
    @GET("people")
    suspend fun getPeople(): PeopleResponseDto

    @GET("people/{id}")
    suspend fun getPeopleById(@Path("id") id: String): HumanDto

    @GET("starships")
    suspend fun getStarships(): StarshipsResponseDto

    @GET("starships/{id}")
    suspend fun getStarshipById(
        @Path("id") id: String
    ): StarshipDto
}
