package ru.student.starwars.domain

import ru.student.starwars.domain.entity.Human

sealed class StarScreenState {
    object Initial : StarScreenState()
    object Loading : StarScreenState()
    class ShowHuman(val human: Human) : StarScreenState()
    class People(val people: List<Human>) : StarScreenState()
}