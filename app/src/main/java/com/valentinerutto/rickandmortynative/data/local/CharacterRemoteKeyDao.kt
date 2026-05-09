package com.valentinerutto.rickandmortynative.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
 interface CharacterRemoteKeyDao{
    @Query("SELECT * FROM character_remote_keys WHERE characterId = :id")
    suspend fun getRemoteKey(id: Int): CharacterRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<CharacterRemoteKey>)

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearAll()
}
