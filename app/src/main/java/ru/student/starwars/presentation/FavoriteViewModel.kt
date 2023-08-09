package ru.student.starwars.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.usecases.ChangeCharacterFavoriteUseCase
import ru.student.starwars.domain.usecases.ChangeStarshipFavoriteUseCase
import ru.student.starwars.domain.usecases.GetFavoriteCharactersUseCase
import ru.student.starwars.domain.usecases.GetFavoriteStarshipsUseCase
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val changeCharacterFavoriteUseCase: ChangeCharacterFavoriteUseCase,
    getFavoriteStarshipsUseCase: GetFavoriteStarshipsUseCase,
    private val changeStarshipFavoriteUseCase: ChangeStarshipFavoriteUseCase
) : ViewModel() {

    private val favoriteCharacters = getFavoriteCharactersUseCase()
        .map { MainScreenState.Characters(it) }

    private val favoriteStarships = getFavoriteStarshipsUseCase()
        .map { MainScreenState.Starships(it) }

    private val nextDataEvents = MutableSharedFlow<MainScreenState>()

    val screenStateFlow = favoriteCharacters
        .mergeWith(favoriteStarships)
        .mergeWith(nextDataEvents)

    fun getFavoriteStarships() {
        viewModelScope.launch {
            favoriteStarships.collect {
                nextDataEvents.emit(it)
            }
        }
    }
    fun getFavoriteCharacters() {
        viewModelScope.launch {
            favoriteCharacters.collect {
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