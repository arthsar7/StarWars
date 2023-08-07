package ru.student.starwars.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.student.starwars.domain.StarScreenState
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.usecases.ChangeHumanFavoriteUseCase
import ru.student.starwars.domain.usecases.GetFavoritePeopleUseCase
import ru.student.starwars.domain.usecases.GetPeopleByIdUseCase
import ru.student.starwars.domain.usecases.GetPeopleUseCase
import javax.inject.Inject

@Suppress("USELESS_CAST")
class MainViewModel @Inject constructor(
    private val getPeopleByIdUseCase: GetPeopleByIdUseCase,
    getPeopleUseCase: GetPeopleUseCase,
    private val changeHumanFavoriteUseCase: ChangeHumanFavoriteUseCase,
    getFavoritePeopleUseCase: GetFavoritePeopleUseCase
) : ViewModel() {
    val peopleFlow = getPeopleUseCase()
        .filter { it.isNotEmpty() }
        .map { StarScreenState.People(it) as StarScreenState }
        .onStart { emit(StarScreenState.Loading) }

    fun getPeopleById(id: String): Flow<StarScreenState> {
        return getPeopleByIdUseCase(id)
            .filter { it != Human(isFavorite = false) }
            .map { StarScreenState.ShowHuman(it) as StarScreenState }
            .onStart { emit(StarScreenState.Loading) }
    }

    fun changeHumanFavorite(human: Human) {
        changeHumanFavoriteUseCase(human)
    }

    val favoritePeople = getFavoritePeopleUseCase()

}