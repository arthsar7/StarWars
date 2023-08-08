package ru.student.starwars.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.student.starwars.domain.entity.Character

interface CharactersRepository {
    fun getCharacterById(id: String): StateFlow<Character>

    fun getCharacters(): StateFlow<List<Character>>

    fun getFavoriteCharacters(): StateFlow<List<Character>>

    fun changeCharacterFavorite(character: Character)
}