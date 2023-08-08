package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.PeopleRepository
import javax.inject.Inject

class GetFavoritePeopleUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke() = repository.getFavoritePeople()
}