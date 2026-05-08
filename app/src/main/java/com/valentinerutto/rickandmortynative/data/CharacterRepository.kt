package com.valentinerutto.rickandmortynative.data

import com.valentinerutto.rickandmortynative.data.local.CharacterDao
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.data.local.toEntity
import com.valentinerutto.rickandmortynative.data.network.ApiService
import com.valentinerutto.rickandmortynative.util.Resource

class CharacterRepository  (  private val api: ApiService,
private val dao: CharacterDao
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

}