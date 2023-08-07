package ru.student.starwars.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.student.starwars.data.mapper.StarMapper
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.room.StarDao
import ru.student.starwars.di.ApplicationScope
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.repository.StarRepository
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@ApplicationScope
class StarRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: StarMapper,
    private val starDao: StarDao
) : StarRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val _people = mutableListOf<Human>()
    private val people: List<Human>
        get() = _people.toList()

    private val loadPeopleFlow = MutableSharedFlow<List<Human>>()

    private val peopleFlow = flow {
        val peopleResponseDto = apiService.getPeople()
        val currPeople = mapper.mapPeopleResponseToEntities(peopleResponseDto).toMutableList()
        currPeople.replaceAll {
            if (starDao.getFavoritePeopleById(it.id) != null) {
                return@replaceAll it.copy(isFavorite = true)
            }
            return@replaceAll it
        }
        _people.addAll(currPeople.toList())
        emit(people)
    }
        .mergeWith(loadPeopleFlow)
        .retry {
            delay(RETRY_MILLIS_TIMESTAMP)
            true
        }
        .stateIn(
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
            initialValue = Human()
        )
    }

    override fun getPeople(): StateFlow<List<Human>> {
        return peopleFlow
    }

    override fun getFavoritePeople(): LiveData<List<Human>> {
        return starDao.getFavoritePeople()
            .map { mapper.mapListHumanDbModelToEntity(it) }
    }

    override fun changeHumanFavorite(human: Human) {
        coroutineScope.launch {
            val requiredDbModel = starDao.getFavoritePeopleById(human.id)
            if (requiredDbModel == null) {
                val newDbModel = mapper.mapHumanEntityToDbModel(human).copy(isFavorite = true)
                starDao.addHumanToFavorites(newDbModel)
            } else {
                starDao.deleteHumanFromFavorites(requiredDbModel.id)
            }
            updatePeopleList(human)
        }
    }

    private suspend fun updatePeopleList(human: Human) {
        _people.replaceAll {
            if (human.id == it.id) {
                return@replaceAll it.copy(isFavorite = !it.isFavorite)
            }
            return@replaceAll it
        }
        loadPeopleFlow.emit(people)
    }

    companion object {
        private const val RETRY_MILLIS_TIMESTAMP = 3000L
    }
}