package ru.student.starwars.data.mapper

import ru.student.starwars.data.models.CharacterDto
import ru.student.starwars.data.models.CharacterResponseDto
import ru.student.starwars.data.room.model.CharacterDbModel
import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.entity.Gender
import javax.inject.Inject

class CharactersMapper @Inject constructor() {
    fun mapDtoModelToEntity(characterDto: CharacterDto): Character {
        return Character(
            id = characterDto.url,
            name = characterDto.name,
            height = characterDto.height,
            mass = characterDto.mass,
            gender = mapGenderToEnum(characterDto.gender),
            skinColor = characterDto.skinColor,
            eyeColor = characterDto.eyeColor,
            imageUrl = "https://cdn.icon-icons.com/icons2/318/PNG/512/Stormtrooper-icon_34494.png",
            isFavorite = false,
            starshipsCount = characterDto.starshipsUrl.size
        )
    }

    private fun mapDbModelToEntity(characterDbModel: CharacterDbModel): Character {
        return Character(
            id = characterDbModel.id,
            name = characterDbModel.name,
            height = characterDbModel.height,
            mass = characterDbModel.mass,
            gender = mapGenderToEnum(characterDbModel.gender),
            skinColor = characterDbModel.skinColor,
            eyeColor = characterDbModel.eyeColor,
            imageUrl = characterDbModel.imageUrl,
            isFavorite = characterDbModel.isFavorite,
            starshipsCount = characterDbModel.starshipsCount
        )
    }

    private fun mapGenderToEnum(gender: String) =
        when (gender) {
            MALE_ATTRIBUTE -> Gender.MALE
            FEMALE_ATTRIBUTE -> Gender.FEMALE
            NEUTRAL_ATTRIBUTE -> Gender.NEUTRAL
            else -> {
                throw IllegalStateException()
            }
        }

    fun mapListDbModelToEntity(listCharacterDbModels: List<CharacterDbModel>): List<Character> {
        return listCharacterDbModels.map {
            mapDbModelToEntity(it)
        }
    }
    fun mapEntityToDbModel(character: Character): CharacterDbModel {
        return CharacterDbModel(
            id = character.id,
            name = character.name,
            height = character.height,
            mass = character.mass,
            gender = mapGenderToString(character.gender),
            skinColor = character.skinColor,
            eyeColor = character.eyeColor,
            imageUrl = character.imageUrl,
            isFavorite = character.isFavorite,
            starshipsCount = character.starshipsCount
        )
    }

    private fun mapGenderToString(gender: Gender) =
        when(gender) {
            Gender.MALE -> MALE_ATTRIBUTE
            Gender.FEMALE -> FEMALE_ATTRIBUTE
            Gender.NEUTRAL -> NEUTRAL_ATTRIBUTE
        }

    fun mapResponseToEntities(characterResponseDto: CharacterResponseDto): List<Character> {
        val characterDtoList = characterResponseDto.characters
        return characterDtoList.map { mapDtoModelToEntity(it) }
    }

    companion object {
        private const val MALE_ATTRIBUTE = "male"
        private const val FEMALE_ATTRIBUTE = "female"
        private const val NEUTRAL_ATTRIBUTE = "n/a"
    }
}