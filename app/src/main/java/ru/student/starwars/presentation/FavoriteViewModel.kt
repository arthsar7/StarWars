package ru.student.starwars.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.usecases.ChangeHumanFavoriteUseCase
import ru.student.starwars.domain.usecases.ChangeStarshipFavoriteUseCase
import ru.student.starwars.domain.usecases.GetFavoritePeopleUseCase
import ru.student.starwars.domain.usecases.GetFavoriteStarshipsUseCase
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject
@Suppress("USELESS_CAST")
class FavoriteViewModel @Inject constructor(
    getFavoritePeopleUseCase: GetFavoritePeopleUseCase,
    private val changeHumanFavoriteUseCase: ChangeHumanFavoriteUseCase,
    getFavoriteStarshipsUseCase: GetFavoriteStarshipsUseCase,
    private val changeStarshipFavoriteUseCase: ChangeStarshipFavoriteUseCase
) : ViewModel() {

    private val favoritePeople = getFavoritePeopleUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.People(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading) }

    private val favoriteStarships = getFavoriteStarshipsUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.Starships(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading) }

    private val nextDataEvents = MutableSharedFlow<MainScreenState>()

    val screenStateFlow = favoritePeople.mergeWith(nextDataEvents)

    fun getFavoriteStarships() {
        viewModelScope.launch {
            favoriteStarships.collect {
                nextDataEvents.emit(MainScreenState.Starships(listOf()))
                nextDataEvents.emit(it)
            }
        }
    }

    fun getFavoritePeople() {
        viewModelScope.launch {
            favoritePeople.collect {
                nextDataEvents.emit(MainScreenState.People(listOf()))
                nextDataEvents.emit(it)
            }
        }
    }

    fun changeHumanFavorite(human: Human) {
        changeHumanFavoriteUseCase(human)
    }

    fun changeStarshipFavorite(starship: Starship) {
        changeStarshipFavoriteUseCase(starship)
    }
}