package ru.student.starwars.domain.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import ru.student.starwars.domain.entity.Human

interface StarRepository {
    fun getPeopleById(id: String): StateFlow<Human>

    fun getPeople(): StateFlow<List<Human>>

    fun getFavoritePeople(): LiveData<List<Human>>

    suspend fun changeHumanFavorite(human: Human)
}