package ru.student.starwars.presentation

import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.entity.Starship

sealed class MainScreenState {
    object Initial : MainScreenState()
    object Loading : MainScreenState()
    data class People(val people: List<Human>) : MainScreenState()
    data class Starships(val starships: List<Starship>) : MainScreenState()
}