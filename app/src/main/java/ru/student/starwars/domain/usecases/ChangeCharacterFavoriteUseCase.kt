package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.repository.CharactersRepository
import javax.inject.Inject

class ChangeCharacterFavoriteUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke(character: Character) {
        repository.changeCharacterFavorite(character)
    }
}