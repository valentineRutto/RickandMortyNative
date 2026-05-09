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
import kotlin.text.isNotBlank

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: String,
    private val status: String?,
    private val species: String?,
    private val api: ApiService,
    private val database: RickandMortyDatabase) : RemoteMediator<Int, CharacterEntity>() {

   private val characterDao = database.characterDao()
  private  val remoteKeyDao = database.remoteKeyDao()

    private val filterKey = buildFilterKey(query, status, species)
    private val isUnfiltered = query.isBlank() && status.isNullOrBlank() && species.isNullOrBlank()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                    val key = firstItem?.let { remoteKeyDao.getRemoteKey(it.id,filterKey) }
                    key?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val key = lastItem?.let { remoteKeyDao.getRemoteKey(it.id,filterKey) }
                    key?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getCharacters(
                page = page,
                name = query.takeIf { it.isNotBlank() },
                status = status,
                species = species
            )
            if (!response.isSuccessful) {
                return if (response.code() == 404) {
                    MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    MediatorResult.Error(IllegalStateException(response.message()))
                }
            }

            val body =response.body()
            val characters = body?.results
            val endReached = body?.info?.next == null

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    if (isUnfiltered) {
                        characterDao.clearAll()
                        remoteKeyDao.clearAll()
                    } else {
                        remoteKeyDao.clearByFilter(filterKey)
                    }
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endReached) null else page + 1

                val keys = characters?.map {
                    CharacterRemoteKey(
                        characterId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        filterKey = filterKey

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

    private companion object {

        fun buildFilterKey(query: String, status: String?, species: String?): String {
            return listOf(
                query.trim().lowercase(),
                status.orEmpty().lowercase(),
                species.orEmpty().lowercase()
            ).joinToString(separator = "|")
        }
    }
}