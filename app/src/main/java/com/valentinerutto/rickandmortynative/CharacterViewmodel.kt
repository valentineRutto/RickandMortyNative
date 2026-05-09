package com.valentinerutto.rickandmortynative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.valentinerutto.rickandmortynative.data.CharacterRepository
import com.valentinerutto.rickandmortynative.data.local.CharacterEntity
import com.valentinerutto.rickandmortynative.data.network.models.Episode
import com.valentinerutto.rickandmortynative.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterViewmodel(
                              private val repository: CharacterRepository
) : ViewModel() {

    private val filters = MutableStateFlow(FilterState())

    val uiState: StateFlow<UiState> = filters
        .map { filter ->
           UiState(
                query = filter.query,
                selectedStatus = filter.status,
                selectedSpecies = filter.species
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState()
        )

    val characters: Flow<PagingData<CharacterEntity>> = filters
        .debounce(250)
        .map { it.copy(query = it.query.trim()) }
        .distinctUntilChanged()
        .flatMapLatest { filter ->
            repository.getPagedCharacters(
                query = filter.query,
                status = filter.status,
                species = filter.species
            )
        }.cachedIn(viewModelScope)


    fun onQueryChange(query: String) {
        filters.update { it.copy(query = query) }
    }

    fun onStatusSelected(status: String?) {
        filters.update { it.copy(status = status) }
    }

    fun onSpeciesSelected(species: String?) {
        filters.update { it.copy(species = species) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun characterDetailState(characterId: Int): Flow<CharacterDetailUiState> {

        return repository.observeCharacter(characterId).flatMapLatest { character ->
            flow {
                val episodeIds = character.firstEpisodeIds()
                emit(
                    CharacterDetailUiState(
                        character = character,
                        episodeIds = episodeIds,
                        isLoadingEpisodes = episodeIds.isNotEmpty()
                    )
                )

                if (episodeIds.isEmpty()) return@flow

                runCatching {
                    repository.getEpisodes(episodeIds)
                }.onSuccess { episodes ->
                    emit(
                        CharacterDetailUiState(
                            character = character,
                            episodeIds = episodeIds,
                            episodes = episodes
                        )
                    )
                }.onFailure {
                    emit(
                        CharacterDetailUiState(
                            character = character,
                            episodeIds = episodeIds,
                            episodeError = "Could not load episode details."
                        )
                    )
                }
            }
        }

    }


    private fun CharacterEntity?.firstEpisodeIds(): List<Int> {
        return this?.episodeUrls
            ?.split(",")
            ?.mapNotNull { it.extractEpisodeId() }
            ?.take(3)
            .orEmpty()
    }

    private fun String.extractEpisodeId(): Int? {
        return Regex("""(\d+)\D*$""")
            .find(trim())
            ?.groupValues
            ?.getOrNull(1)
            ?.toIntOrNull()
    }

}
private data class FilterState(
    val query: String = "",
    val status: String? = "Alive",
    val species: String? = null
)

data class UiState(
    val query: String = "",
    val selectedStatus: String? = "Alive",
    val selectedSpecies: String? = null
)


data class CharacterDetailUiState(
    val character: CharacterEntity? = null,
    val episodeIds: List<Int> = emptyList(),
    val episodes: List<Episode> = emptyList(),
    val isLoadingEpisodes: Boolean = false,
    val episodeError: String? = null
)






