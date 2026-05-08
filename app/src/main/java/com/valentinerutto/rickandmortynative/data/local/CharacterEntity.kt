package com.valentinerutto.rickandmortynative.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valentinerutto.rickandmortynative.data.network.models.CharacterResponse
import com.valentinerutto.rickandmortynative.data.network.models.Result


@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val image: String
)

fun Result.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        origin = origin.name,
        location = location.name,
        image = image
    )

}