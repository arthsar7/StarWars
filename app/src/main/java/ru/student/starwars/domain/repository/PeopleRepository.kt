package ru.student.starwars.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.student.starwars.domain.entity.Human

interface PeopleRepository {
    fun getPeopleById(id: String): StateFlow<Human>

    fun getPeople(): StateFlow<List<Human>>

    fun getFavoritePeople(): StateFlow<List<Human>>

    fun changeHumanFavorite(human: Human)
}