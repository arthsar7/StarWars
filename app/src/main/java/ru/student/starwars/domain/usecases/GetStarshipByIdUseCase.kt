package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.StarshipsRepository
import javax.inject.Inject

class GetStarshipByIdUseCase @Inject constructor(
    private val repository: StarshipsRepository
) {
    operator fun invoke(id: String) = repository.getStarshipById(id)
}