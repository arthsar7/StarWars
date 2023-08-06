package ru.student.starwars.data.mapper

import ru.student.starwars.data.models.HumanDto
import ru.student.starwars.data.models.PeopleResponseDto
import ru.student.starwars.data.room.HumanDbModel
import ru.student.starwars.domain.entity.Human

class StarMapper {
    fun mapDtoModelToEntity(humanDto: HumanDto): Human {
        return Human(
            id = humanDto.url,
            name = humanDto.name,
            height = humanDto.height,
            mass = humanDto.mass,
            gender = humanDto.gender,
            skinColor = humanDto.skinColor,
            eyeColor = humanDto.eyeColor,
            imageUrl = "https://cdn.icon-icons.com/icons2/318/PNG/512/Stormtrooper-icon_34494.png",
            isFavorite = false
        )
    }

    fun mapHumanDbModelToEntity(humanDbModel: HumanDbModel): Human {
        return Human(
            id = humanDbModel.id,
            name = humanDbModel.name,
            height = humanDbModel.height,
            mass = humanDbModel.mass,
            gender = humanDbModel.gender,
            skinColor = humanDbModel.skinColor,
            eyeColor = humanDbModel.eyeColor,
            imageUrl = humanDbModel.imageUrl,
            isFavorite = humanDbModel.isFavorite
        )
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
            gender = human.gender,
            skinColor = human.skinColor,
            eyeColor = human.eyeColor,
            imageUrl = human.imageUrl,
            isFavorite = human.isFavorite
        )
    }
    fun mapPeopleResponseToEntities(peopleResponseDto: PeopleResponseDto): List<Human> {
        val peopleDto = peopleResponseDto.results
        return peopleDto.map { mapDtoModelToEntity(it) }
    }
}