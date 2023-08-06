package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.StarRepository

class GetPeopleByIdUseCase(
    private val repository: StarRepository
) {
    operator fun invoke(id: String) = repository.getPeopleById(id)
}