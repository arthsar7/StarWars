package ru.student.starwars.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.student.starwars.data.mapper.PeopleMapper
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.room.dao.PeopleDao
import ru.student.starwars.di.ApplicationScope
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.repository.PeopleRepository
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@ApplicationScope
class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: PeopleMapper,
    private val peopleDao: PeopleDao
) : PeopleRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val _people = mutableListOf<Human>()
    private val people: List<Human>
        get() = _people.toList()

    private val loadPeopleFlow = MutableSharedFlow<List<Human>>()

    private val peopleFlow = flow {
        val peopleResponseDto = apiService.getPeople()
        val currPeople = mapper.mapPeopleResponseToEntities(peopleResponseDto).toMutableList()
        currPeople.replaceAll {
            if (peopleDao.getFavoritePeopleById(it.id) != null) {
                Log.d("NO", "HERE WE")

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

    override fun getFavoritePeople(): StateFlow<List<Human>> {
        return peopleDao
            .getFavoritePeople()
            .map { mapper.mapListHumanDbModelToEntity(it) }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }

    override fun changeHumanFavorite(human: Human) {
        coroutineScope.launch {
            val requiredEntity = _people.find { it.id == human.id }
            if (requiredEntity?.isFavorite == false) {
                val newDbModel = mapper.mapHumanEntityToDbModel(human).copy(isFavorite = true)
                peopleDao.addHumanToFavorites(newDbModel)
            } else {
                requiredEntity?.id?.let { peopleDao.deleteHumanFromFavorites(it) }
            }
            _people.replaceAll {
                if (human.id == it.id) {
                    return@replaceAll it.copy(isFavorite = !it.isFavorite)
                }
                return@replaceAll it
            }
            loadPeopleFlow.emit(people)
        }
    }

    companion object {
        private const val RETRY_MILLIS_TIMESTAMP = 1000L
    }
}