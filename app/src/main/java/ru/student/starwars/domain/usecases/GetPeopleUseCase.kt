package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.PeopleRepository
import javax.inject.Inject

class GetPeopleUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    operator fun invoke() = repository.getPeople()
}