package com.valentinerutto.rickandmortynative.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys",
    primaryKeys = ["characterId", "filterKey"]
)
data class CharacterRemoteKey(
     val characterId: Int,
    val filterKey: String,
    val prevKey: Int?,
    val nextKey: Int?
)