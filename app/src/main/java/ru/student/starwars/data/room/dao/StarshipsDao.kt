package ru.student.starwars.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.student.starwars.data.room.model.StarshipDbModel

@Dao
interface StarshipsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStarshipToFavorites(starshipDbModel: StarshipDbModel)

    @Query("DELETE FROM favorite_starships WHERE id =:starshipId")
    suspend fun deleteStarshipFromFavorites(starshipId: String)


    @Query("SELECT * FROM favorite_starships WHERE id=:starshipId LIMIT 1")
    fun getFavoriteStarshipById(starshipId: String): StarshipDbModel?

    @Query("SELECT * FROM favorite_starships")
    fun getFavoriteStarships(): Flow<List<StarshipDbModel>>
}
