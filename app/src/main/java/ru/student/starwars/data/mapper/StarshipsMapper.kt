package ru.student.starwars.data.mapper

import ru.student.starwars.data.models.StarshipDto
import ru.student.starwars.data.room.model.StarshipDbModel
import ru.student.starwars.domain.entity.Starship
import javax.inject.Inject

class StarshipsMapper @Inject constructor() {
    fun mapStarshipDtoToEntity(starshipDto: StarshipDto): Starship {
        return Starship(
            id = starshipDto.url,
            name = starshipDto.name,
            model = starshipDto.model,
            manufacturer = starshipDto.manufacturer,
            passengers = starshipDto.passengers
        )
    }

    fun mapStarshipDtoListToEntity(starshipDtoList: List<StarshipDto>): List<Starship> {
        return starshipDtoList.map { mapStarshipDtoToEntity(it) }
    }
    fun mapStarshipDbModelListToEntity(starshipDbModelList: List<StarshipDbModel>): List<Starship> {
        return starshipDbModelList.map { mapStarshipDbModelToEntity(it) }
    }

    fun mapStarshipEntityToDbModel(starship: Starship): StarshipDbModel {
        return StarshipDbModel(
            id = starship.id,
            name = starship.name,
            model = starship.model,
            manufacturer = starship.manufacturer,
            passengers = starship.passengers,
            isFavorite = starship.isFavorite
        )
    }

    fun mapStarshipDbModelToEntity(starship: StarshipDbModel): Starship {
        return Starship(
            id = starship.id,
            name = starship.name,
            model = starship.model,
            manufacturer = starship.manufacturer,
            passengers = starship.passengers,
            isFavorite = starship.isFavorite
        )
    }
}