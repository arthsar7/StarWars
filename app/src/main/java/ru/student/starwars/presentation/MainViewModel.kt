package ru.student.starwars.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.usecases.ChangeCharacterFavoriteUseCase
import ru.student.starwars.domain.usecases.ChangeStarshipFavoriteUseCase
import ru.student.starwars.domain.usecases.GetCharactersUseCase
import ru.student.starwars.domain.usecases.GetStarshipsUseCase
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@Suppress("USELESS_CAST")
class MainViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val changeCharacterFavoriteUseCase: ChangeCharacterFavoriteUseCase,
    getStarshipsUseCase: GetStarshipsUseCase,
    private val changeStarshipFavoriteUseCase: ChangeStarshipFavoriteUseCase
) : ViewModel() {

    private val charactersFlow = getCharactersUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.Characters(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading) }

    private val starshipsFlow = getStarshipsUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.Starships(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading)  }

    private val nextDataEvents = MutableSharedFlow<MainScreenState>()

    val screenStateFlow = charactersFlow.mergeWith(starshipsFlow).mergeWith(nextDataEvents)

    fun getStarships() {
        viewModelScope.launch {
            starshipsFlow.collect {
                nextDataEvents.emit(MainScreenState.Starships(listOf()))
                nextDataEvents.emit(it)
            }
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            charactersFlow.collect {
                nextDataEvents.emit(MainScreenState.Characters(listOf()))
                nextDataEvents.emit(it)
            }
        }
    }

    fun changeCharacterFavorite(character: Character) {
        changeCharacterFavoriteUseCase(character)
    }

    fun changeStarshipFavorite(starship: Starship) {
        changeStarshipFavoriteUseCase(starship)
    }

}