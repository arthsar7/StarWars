package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.repository.CharactersRepository
import javax.inject.Inject

class GetFavoriteCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke() = repository.getFavoriteCharacters()
}