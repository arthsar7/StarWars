package ru.student.starwars.presentation

import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.entity.Starship

sealed class MainScreenState {
    object Initial : MainScreenState()
    object Loading : MainScreenState()
    data class Characters(val characters: List<Character>) : MainScreenState()
    data class Starships(val starships: List<Starship>) : MainScreenState()
}