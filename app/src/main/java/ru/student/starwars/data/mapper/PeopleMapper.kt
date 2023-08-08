package ru.student.starwars.data.mapper

import ru.student.starwars.data.models.HumanDto
import ru.student.starwars.data.models.PeopleResponseDto
import ru.student.starwars.data.room.model.HumanDbModel
import ru.student.starwars.domain.entity.Gender
import ru.student.starwars.domain.entity.Human
import javax.inject.Inject

class PeopleMapper @Inject constructor() {
    fun mapDtoModelToEntity(humanDto: HumanDto): Human {
        return Human(
            id = humanDto.url,
            name = humanDto.name,
            height = humanDto.height,
            mass = humanDto.mass,
            gender = mapGenderToEnum(humanDto.gender),
            skinColor = humanDto.skinColor,
            eyeColor = humanDto.eyeColor,
            imageUrl = "https://cdn.icon-icons.com/icons2/318/PNG/512/Stormtrooper-icon_34494.png",
            isFavorite = false,
            starshipsCount = humanDto.starshipsUrl.size
        )
    }

    private fun mapHumanDbModelToEntity(humanDbModel: HumanDbModel): Human {
        return Human(
            id = humanDbModel.id,
            name = humanDbModel.name,
            height = humanDbModel.height,
            mass = humanDbModel.mass,
            gender = mapGenderToEnum(humanDbModel.gender),
            skinColor = humanDbModel.skinColor,
            eyeColor = humanDbModel.eyeColor,
            imageUrl = humanDbModel.imageUrl,
            isFavorite = humanDbModel.isFavorite,
            starshipsCount = humanDbModel.starshipsCount
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

    fun mapListHumanDbModelToEntity(listHumanDbModels: List<HumanDbModel>): List<Human> {
        return listHumanDbModels.map {
            mapHumanDbModelToEntity(it)
        }
    }
    fun mapHumanEntityToDbModel(human: Human): HumanDbModel {
        return HumanDbModel(
            id = human.id,
            name = human.name,
            height = human.height,
            mass = human.mass,
            gender = mapGenderToString(human.gender),
            skinColor = human.skinColor,
            eyeColor = human.eyeColor,
            imageUrl = human.imageUrl,
            isFavorite = human.isFavorite,
            starshipsCount = human.starshipsCount
        )
    }

    private fun mapGenderToString(gender: Gender) =
        when(gender) {
            Gender.MALE -> MALE_ATTRIBUTE
            Gender.FEMALE -> FEMALE_ATTRIBUTE
            Gender.NEUTRAL -> NEUTRAL_ATTRIBUTE
        }

    fun mapPeopleResponseToEntities(peopleResponseDto: PeopleResponseDto): List<Human> {
        val peopleDto = peopleResponseDto.people
        return peopleDto.map { mapDtoModelToEntity(it) }
    }

    companion object {
        private const val MALE_ATTRIBUTE = "male"
        private const val FEMALE_ATTRIBUTE = "female"
        private const val NEUTRAL_ATTRIBUTE = "n/a"
    }
}