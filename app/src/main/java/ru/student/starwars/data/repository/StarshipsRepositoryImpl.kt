package ru.student.starwars.data.repository

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
import ru.student.starwars.data.mapper.StarshipsMapper
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.room.dao.StarshipsDao
import ru.student.starwars.di.ApplicationScope
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.repository.StarshipsRepository
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@ApplicationScope
class StarshipsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: StarshipsMapper,
    private val starshipsDao: StarshipsDao
) : StarshipsRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _starships = mutableListOf<Starship>()
    private val starships: List<Starship>
        get() = _starships.toList()

    private val loadStarshipFlow = MutableSharedFlow<List<Starship>>()

    private val starshipsStateFlow = flow {
        val starshipsDto = apiService.getStarships().starships
        val currStarships = mapper.mapStarshipDtoListToEntity(starshipsDto).toMutableList()
        currStarships.replaceAll {
            if (starshipsDao.getFavoriteStarshipById(it.id) != null) {
                return@replaceAll it.copy(isFavorite = true)
            }
            return@replaceAll it
        }
        _starships.addAll(currStarships.toList())
        emit(starships)
    }
        .mergeWith(loadStarshipFlow)
        .retry {
            delay(RETRY_MILLIS_TIMESTAMP)
            true
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = starships
        )

    override fun getStarships(): StateFlow<List<Starship>> {
        return starshipsStateFlow
    }

    override fun getStarshipById(id: String): StateFlow<Starship> =
        flow {
            val starshipDto = apiService.getStarshipById(id)
            val starship = mapper.mapStarshipDtoToEntity(starshipDto)
            emit(starship)
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = Starship()
        )

    override fun changeStarshipFavorite(starship: Starship) {
        coroutineScope.launch {
            val requiredEntity = _starships.find { it.id == starship.id }
            if (requiredEntity?.isFavorite == false) {
                val dbModel = mapper.mapStarshipEntityToDbModel(starship).copy(isFavorite = true)
                starshipsDao.addStarshipToFavorites(dbModel)
            } else {
                requiredEntity?.let { starshipsDao.deleteStarshipFromFavorites(it.id) }
            }
            _starships.replaceAll {
                if (it.id == starship.id) {
                    return@replaceAll it.copy(isFavorite = !it.isFavorite)
                }
                return@replaceAll it
            }
            loadStarshipFlow.emit(starships)
        }
    }

    override fun getFavoriteStarships(): StateFlow<List<Starship>> {
        return starshipsDao.getFavoriteStarships()
            .map { mapper.mapStarshipDbModelListToEntity(it) }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }

    companion object {
        private const val RETRY_MILLIS_TIMESTAMP = 1000L
    }
}