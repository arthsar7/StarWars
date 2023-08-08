package ru.student.starwars.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.student.starwars.data.models.CharacterDto
import ru.student.starwars.data.models.CharacterResponseDto
import ru.student.starwars.data.models.StarshipDto
import ru.student.starwars.data.models.StarshipsResponseDto

interface ApiService {
    @GET("people")
    suspend fun getCharacters(): CharacterResponseDto

    @GET("people/{id}")
    suspend fun getCharactersById(@Path("id") id: String): CharacterDto

    @GET("starships")
    suspend fun getStarships(): StarshipsResponseDto

    @GET("starships/{id}")
    suspend fun getStarshipById(
        @Path("id") id: String
    ): StarshipDto
}
