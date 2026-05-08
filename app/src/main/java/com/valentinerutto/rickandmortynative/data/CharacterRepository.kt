package com.valentinerutto.rickandmortynative.data

import com.valentinerutto.rickandmortynative.data.local.CharacterDao
import com.valentinerutto.rickandmortynative.data.network.ApiService

class CharacterRepository  (  private val api: ApiService,
private val dao: CharacterDao
) {


}