package ru.student.starwars.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.student.starwars.data.room.model.CharacterDbModel

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacterToFavorites(characterDbModel: CharacterDbModel)

    @Query("DELETE FROM favorite_characters WHERE id =:characterId")
    suspend fun deleteCharacterFromFavorites(characterId: String)

    @Query("SELECT * FROM favorite_characters")
    fun getFavoriteCharacters(): Flow<List<CharacterDbModel>>

    @Query("SELECT * FROM favorite_characters WHERE id=:characterId LIMIT 1")
    fun getFavoriteCharacterById(characterId: String): CharacterDbModel?

}