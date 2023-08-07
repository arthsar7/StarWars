package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.StarRepository
import javax.inject.Inject

class GetFavoritePeopleUseCase @Inject constructor(
    private val repository: StarRepository
) {
    operator fun invoke() = repository.getFavoritePeople()
}