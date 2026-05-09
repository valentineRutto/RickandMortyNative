package com.valentinerutto.rickandmortynative.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
 interface CharacterRemoteKeyDao{
   @Query("SELECT * FROM character_remote_keys WHERE characterId = :id AND filterKey = :filterKey")
   suspend fun getRemoteKey(id: Int, filterKey: String): CharacterRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<CharacterRemoteKey>)

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearAll()
   @Query("DELETE FROM character_remote_keys WHERE filterKey = :filterKey")
   suspend fun clearByFilter(filterKey: String)
}
