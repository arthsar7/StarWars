package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.repository.StarRepository

class ChangeHumanFavoriteUseCase(
    private val repository: StarRepository
) {
    suspend operator fun invoke(human: Human) {
        repository.changeHumanFavorite(human)
    }
}