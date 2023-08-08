package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.PeopleRepository
import javax.inject.Inject

class GetPeopleByIdUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke(id: String) = repository.getPeopleById(id)
}