package ru.student.starwars.domain.usecases

import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.repository.StarshipsRepository
import javax.inject.Inject

class ChangeStarshipFavoriteUseCase @Inject constructor(
    private val repository: StarshipsRepository
) {
    operator fun invoke(starship: Starship) {
        repository.changeStarshipFavorite(starship)
    }
}