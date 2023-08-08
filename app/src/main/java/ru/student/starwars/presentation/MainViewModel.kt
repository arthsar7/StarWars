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
import ru.student.starwars.domain.usecases.GetPeopleUseCase
import ru.student.starwars.domain.usecases.GetStarshipsUseCase
import ru.student.starwars.extensions.mergeWith
import javax.inject.Inject

@Suppress("USELESS_CAST")
class MainViewModel @Inject constructor(
    getPeopleUseCase: GetPeopleUseCase,
    private val changeHumanFavoriteUseCase: ChangeHumanFavoriteUseCase,
    getStarshipsUseCase: GetStarshipsUseCase,
    private val changeStarshipFavoriteUseCase: ChangeStarshipFavoriteUseCase
) : ViewModel() {

    private val peopleFlow = getPeopleUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.People(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading) }

    private val starshipsFlow = getStarshipsUseCase()
        .filter { it.isNotEmpty() }
        .map { MainScreenState.Starships(it) as MainScreenState }
        .onStart { emit(MainScreenState.Loading)  }

    private val nextDataEvents = MutableSharedFlow<MainScreenState>()

    val screenStateFlow = peopleFlow.mergeWith(nextDataEvents)

    fun getStarships() {
        viewModelScope.launch {
            starshipsFlow.collect {
                nextDataEvents.emit(MainScreenState.Starships(listOf()))
                nextDataEvents.emit(it)
            }
        }
    }

    fun getPeople() {
        viewModelScope.launch {
            peopleFlow.collect {
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