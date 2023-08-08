package ru.student.starwars.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.student.starwars.data.room.model.HumanDbModel

@Dao
interface PeopleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHumanToFavorites(humanDbModel: HumanDbModel)

    @Query("DELETE FROM favorite_people WHERE id =:humanId")
    suspend fun deleteHumanFromFavorites(humanId: String)

    @Query("SELECT * FROM favorite_people")
    fun getFavoritePeople(): Flow<List<HumanDbModel>>

    @Query("SELECT * FROM favorite_people WHERE id=:humanId LIMIT 1")
    fun getFavoritePeopleById(humanId: String): HumanDbModel?

}