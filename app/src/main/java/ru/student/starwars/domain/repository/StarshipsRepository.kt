package ru.student.starwars.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.student.starwars.domain.entity.Starship

interface StarshipsRepository {
    fun getStarships(): StateFlow<List<Starship>>
    fun getStarshipById(id: String): StateFlow<Starship>
    fun changeStarshipFavorite(starship: Starship)
    fun getFavoriteStarships(): StateFlow<List<Starship>>
}