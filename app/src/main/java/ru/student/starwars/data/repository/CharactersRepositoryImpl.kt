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
import ru.student.starwars.data.mapper.CharactersMapper
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.room.dao.CharactersDao
import ru.student.starwars.di.ApplicationScope
import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.repository.CharactersRepository
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@ApplicationScope
class CharactersRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CharactersMapper,
    private val charactersDao: CharactersDao
) : CharactersRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val _characters = mutableListOf<Character>()
    private val characters: List<Character>
        get() = _characters.toList()

    private val loadCharactersFlow = MutableSharedFlow<List<Character>>()

    private val charactersFlow = flow {
        val charactersResponseDto = apiService.getCharacters()
        val currPeople = mapper.mapResponseToEntities(charactersResponseDto).toMutableList()
        currPeople.replaceAll {
            if (charactersDao.getFavoriteCharacterById(it.id) != null) {
                Log.d("NO", "HERE WE")

                return@replaceAll it.copy(isFavorite = true)
            }
            return@replaceAll it
        }
        _characters.addAll(currPeople.toList())
        emit(characters)
    }
        .mergeWith(loadCharactersFlow)
        .retry {
            delay(RETRY_MILLIS_TIMESTAMP)
            true
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = characters
        )

    override fun getCharacterById(id: String): StateFlow<Character> {
        return flow {
            emit(mapper.mapDtoModelToEntity(apiService.getCharactersById(id)))
        }.retry {
            delay(RETRY_MILLIS_TIMESTAMP)
            true
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = Character()
        )
    }

    override fun getCharacters(): StateFlow<List<Character>> {
        return charactersFlow
    }

    override fun getFavoriteCharacters(): StateFlow<List<Character>> {
        return charactersDao
            .getFavoriteCharacters()
            .map { mapper.mapListDbModelToEntity(it) }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }

    override fun changeCharacterFavorite(character: Character) {
        coroutineScope.launch {
            val requiredEntity = _characters.find { it.id == character.id }
            if (requiredEntity?.isFavorite == false) {
                val newDbModel = mapper.mapEntityToDbModel(character).copy(isFavorite = true)
                charactersDao.addCharacterToFavorites(newDbModel)
            } else {
                requiredEntity?.id?.let { charactersDao.deleteCharacterFromFavorites(it) }
            }
            _characters.replaceAll {
                if (character.id == it.id) {
                    return@replaceAll it.copy(isFavorite = !it.isFavorite)
                }
                return@replaceAll it
            }
            loadCharactersFlow.emit(characters)
        }
    }

    companion object {
        private const val RETRY_MILLIS_TIMESTAMP = 1000L
    }
}