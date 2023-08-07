package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.StarRepository
import javax.inject.Inject

class GetPeopleByIdUseCase @Inject constructor(
    private val repository: StarRepository
) {
    operator fun invoke(id: String) = repository.getPeopleById(id)
}