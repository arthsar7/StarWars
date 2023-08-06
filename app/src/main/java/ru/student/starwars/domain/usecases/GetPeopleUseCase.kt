package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.StarRepository

class GetPeopleUseCase(
    private val repository: StarRepository
) {
    operator fun invoke() = repository.getPeople()
}