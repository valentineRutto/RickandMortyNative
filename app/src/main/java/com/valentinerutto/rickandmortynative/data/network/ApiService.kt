package com.valentinerutto.rickandmortynative.data.network

import com.valentinerutto.rickandmortynative.data.network.models.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int,
                              @Query("name") name: String? = null,
                              @Query("status") status: String? = null,
                              @Query("species") species: String? = null): Response<CharacterResponse>

}