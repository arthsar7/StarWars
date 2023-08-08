package ru.student.starwars.presentation

import androidx.lifecycle.ViewModel
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.domain.usecases.ChangeHumanFavoriteUseCase
import ru.student.starwars.domain.usecases.ChangeStarshipFavoriteUseCase
import ru.student.starwars.domain.usecases.GetFavoritePeopleUseCase
import ru.student.starwars.domain.usecases.GetFavoriteStarshipsUseCase
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    getFavoritePeopleUseCase: GetFavoritePeopleUseCase,
    private val changeHumanFavoriteUseCase: ChangeHumanFavoriteUseCase,
    getFavoriteStarshipsUseCase: GetFavoriteStarshipsUseCase,
    private val changeStarshipFavoriteUseCase: ChangeStarshipFavoriteUseCase
) : ViewModel() {
    val favoritePeople = getFavoritePeopleUseCase()
    val favoriteStarships = getFavoriteStarshipsUseCase()

    fun changeHumanFavorite(human: Human) {
        changeHumanFavoriteUseCase(human)
    }

    fun changeStarshipFavorite(starship: Starship) {
        changeStarshipFavoriteUseCase(starship)
    }
}