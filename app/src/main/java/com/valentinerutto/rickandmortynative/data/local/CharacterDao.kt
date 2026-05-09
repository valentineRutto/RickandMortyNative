package com.valentinerutto.rickandmortynative.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCharacters(characters: List<CharacterEntity>)

    @Query(
        """
        SELECT * FROM characters
        WHERE (:query = '' OR name LIKE '%' || :query || '%')
        AND (:status = '' OR LOWER(status) = LOWER(:status))
        AND (:species = '' OR LOWER(species) = LOWER(:species))
        ORDER BY id ASC
        """
    )    fun pagingSource(query: String, status: String, species: String): PagingSource<Int, CharacterEntity>


    @Query(
        """
        SELECT COUNT(*) FROM characters
        WHERE (:name = '' OR name LIKE '%' || :name || '%')
        AND (:status = '' OR LOWER(status) = LOWER(:status))
        AND (:species = '' OR LOWER(species) = LOWER(:species))
        """
    )
    suspend fun matchingCharacterCount(name: String, status: String, species: String): Int

    @Query("DELETE FROM characters")
    suspend fun clearAll()

    @Query("SELECT * FROM characters WHERE id = :id")
    fun observeCharacter(id: Int): Flow<CharacterEntity?>


    @Query("""
        SELECT * FROM characters
        WHERE (:name = '' OR LOWER(name) LIKE '%' || LOWER(:name) || '%')
        AND (:status IS NULL OR LOWER(status) = LOWER(:status))
        AND (:species = '' OR LOWER(species) LIKE '%' || LOWER(:species) || '%')
        ORDER BY id ASC
    """)
    fun searchCharacters(
        name: String,
        status: String?,
        species: String
    ): PagingSource<Int, CharacterEntity>
}