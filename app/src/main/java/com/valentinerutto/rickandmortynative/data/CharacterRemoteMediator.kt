package com.valentinerutto.rickandmortynative.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.data.local.CharacterRemoteKey
import com.valentinerutto.rickandmortynative.data.local.RickandMortyDatabase
import com.valentinerutto.rickandmortynative.data.local.toEntity
import com.valentinerutto.rickandmortynative.data.network.ApiService

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(private val api: ApiService, private val database: RickandMortyDatabase) : RemoteMediator<Int, CharacterEntity>() {

   private val characterDao = database.characterDao()
  private  val remoteKeyDao = database.remoteKeyDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                    val key = firstItem?.let { remoteKeyDao.getRemoteKey(it.id) }
                    key?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val key = lastItem?.let { remoteKeyDao.getRemoteKey(it.id) }
                    key?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getCharacters(page).body()
            val characters = response?.results
            val endReached = response?.info?.next == null

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endReached) null else page + 1

                val keys = characters?.map {
                    CharacterRemoteKey(
                        characterId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                if (keys != null) {
                    remoteKeyDao.insertAll(keys)
                }
          if (!characters.isNullOrEmpty()){
                characterDao.upsertCharacters(characters.map { it.toEntity() })}
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }
}