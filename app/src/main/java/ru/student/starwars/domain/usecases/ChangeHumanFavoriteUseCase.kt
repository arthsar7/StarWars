package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.repository.PeopleRepository
import javax.inject.Inject

class ChangeHumanFavoriteUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke(human: Human) {
        repository.changeHumanFavorite(human)
    }
}