package com.valentinerutto.rickandmortynative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.cachedIn
import com.valentinerutto.rickandmortynative.data.CharacterRepository
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.util.Resource
import kotlinx.coroutines.launch

class CharacterViewmodel( private val repository: CharacterRepository
) : ViewModel() {
    val characters = repository
        .getPagedCharacters()
        .cachedIn(viewModelScope)
suspend fun getCharacters(): Resource<List<CharacterEntity>> {
val response = repository.getCharacters()
    return response
}








}