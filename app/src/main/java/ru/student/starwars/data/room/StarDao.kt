package ru.student.starwars.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHumanToFavorites(humanDbModel: HumanDbModel)

    @Query("DELETE FROM favorite_people WHERE id =:s")
    suspend fun deleteHumanFromFavorites(s: String)

    @Query("SELECT * FROM favorite_people")
    fun getFavoritePeople(): LiveData<List<HumanDbModel>>

    @Query("SELECT * FROM favorite_people WHERE id=:humanName LIMIT 1")
    fun getFavoritePeopleById(humanName: String): HumanDbModel?

}