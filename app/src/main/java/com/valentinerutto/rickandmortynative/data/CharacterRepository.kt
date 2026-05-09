package com.valentinerutto.rickandmortynative.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.valentinerutto.rickandmortynative.data.local.CharacterDao
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.data.local.RickandMortyDatabase
import com.valentinerutto.rickandmortynative.data.local.toEntity
import com.valentinerutto.rickandmortynative.data.network.ApiService
import com.valentinerutto.rickandmortynative.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class CharacterRepository  (  private val api: ApiService,
private val dao: CharacterDao, private val database: RickandMortyDatabase
) {
    suspend fun getCharacters(): Resource<List<CharacterEntity>> {
        val response = api.getCharacters(
            page = 1
        )

        return (if (response.isSuccessful && response.body() != null){
            val characters = response.body()!!.results.map { it.toEntity() }
            dao.upsertCharacters(characters)
            Resource.Success(response.body()!!.results.map { it.toEntity() })
        }else{
            Resource.Error(response.message())
        })

    }

    @OptIn(ExperimentalPagingApi::class)
   suspend fun getPagedCharacters(
        query: String?=null,
                                 status: String?=null,
                                 species: String?=null): Flow<PagingData<CharacterEntity>>{

        val normalizedQuery = query?.trim().orEmpty()
        val normalizedStatus = status?.takeIf { it.isNotBlank() }
        val normalizedSpecies = species?.takeIf { it.isNotBlank() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                api = api,
                database = database,
                query = normalizedQuery,
                status = normalizedStatus,
                species = normalizedSpecies
            ),
            pagingSourceFactory = {
                dao.searchCharacters(
                    name = normalizedQuery,
                    status = normalizedStatus,
                    species = normalizedSpecies.orEmpty()
                )
            }
        ).flow
   }

}