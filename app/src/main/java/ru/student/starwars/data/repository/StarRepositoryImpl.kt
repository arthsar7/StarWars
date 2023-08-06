package ru.student.starwars.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.student.starwars.data.mapper.StarMapper
import ru.student.starwars.data.network.ApiFactory
import ru.student.starwars.data.room.AppDatabase
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.repository.StarRepository

class StarRepositoryImpl(
    application: Application
) : StarRepository {
    private val starDao = AppDatabase.getInstance(application).starDao()
    private val apiService = ApiFactory.apiService
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val mapper = StarMapper()
    private val _people = mutableListOf<Human>()
    private val people: List<Human>
        get() = _people.toList()

    private val peopleFlow = flow {
        val peopleResponseDto = apiService.getPeople()
        val currPeople = mapper.mapPeopleResponseToEntities(peopleResponseDto)
        _people.addAll(currPeople)
        emit(people)
    }.retry {
        delay(RETRY_MILLIS_TIMESTAMP)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = people
    )

    override fun getPeopleById(id: String): StateFlow<Human> {
        return flow {
            emit(mapper.mapDtoModelToEntity(apiService.getPeopleById(id)))
        }.retry {
            delay(RETRY_MILLIS_TIMESTAMP)
            true
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = Human(isFavorite = false)
        )
    }

    override fun getPeople(): StateFlow<List<Human>> {
        return peopleFlow
    }

    override fun getFavoritePeople(): LiveData<List<Human>> = starDao.getFavoritePeople()
        .map { mapper.mapListHumanDbModelToEntity(it) }.also {
            Log.d("repo", it.value.toString())
        }

    override suspend fun changeHumanFavorite(human: Human) {
        coroutineScope.launch(Dispatchers.IO) {
            val required = starDao.getFavoritePeopleById(human.id)
            if (required == null) {
                starDao.addHumanToFavorites(mapper.mapHumanEntityToDbModel(human).copy(isFavorite = true))
            }
            else {
                starDao.deleteHumanFromFavorites(mapper.mapHumanEntityToDbModel(human).id)
            }
        }

    }

    companion object {
        private const val RETRY_MILLIS_TIMESTAMP = 3000L
    }
}