package com.valentinerutto.rickandmortynative.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valentinerutto.rickandmortynative.data.network.models.CharacterResponse


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
