package ru.student.starwars.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.student.starwars.data.repository.StarRepositoryImpl
import ru.student.starwars.domain.StarScreenState
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.usecases.ChangeHumanFavoriteUseCase
import ru.student.starwars.domain.usecases.GetFavoritePeopleUseCase
import ru.student.starwars.domain.usecases.GetPeopleByIdUseCase
import ru.student.starwars.domain.usecases.GetPeopleUseCase

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository = StarRepositoryImpl(application)
    private val getPeopleByIdUseCase = GetPeopleByIdUseCase(repository)
    private val getPeopleUseCase = GetPeopleUseCase(repository)
    private val changeHumanFavoriteUseCaseUseCase = ChangeHumanFavoriteUseCase(repository)
    private val getFavoritePeopleUseCase = GetFavoritePeopleUseCase(repository)
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
        viewModelScope.launch {
            changeHumanFavoriteUseCaseUseCase(human)
        }
    }
    fun getFavoritePeople()  = getFavoritePeopleUseCase()

}