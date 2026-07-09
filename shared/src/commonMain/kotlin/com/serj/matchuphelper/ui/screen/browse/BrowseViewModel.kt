package com.serj.matchuphelper.ui.screen.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.ChampionRepository
import com.serj.matchuphelper.domain.repository.MatchupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowseViewModel(
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    private var allChampions = emptyMap<String, Champion>()

    init {
        viewModelScope.launch {
            championRepository.getAllChampions().collect { champions ->
                allChampions = champions.associateBy { it.id }
            }
        }
        loadAllMatchups()
    }

    private fun loadAllMatchups() {
        viewModelScope.launch {
            matchupRepository.getRecentReviews(1000).collect { reviews ->
                // We need matchups, not reviews — collect unique matchup IDs
            }
        }
        // Load matchups by iterating the database
        viewModelScope.launch {
            // Get all matchups across all roles
            val matchups = mutableListOf<Matchup>()
            Role.entries.forEach { role ->
                matchupRepository.getMatchupsByRole(role).collect { roleMatchups ->
                    matchups.addAll(roleMatchups)
                    updateFilteredMatchups(matchups.distinctBy { it.id })
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun setRoleFilter(role: Role?) {
        _uiState.update { it.copy(roleFilter = role) }
        if (role != null) {
            viewModelScope.launch {
                matchupRepository.getMatchupsByRole(role).collect { matchups ->
                    updateFilteredMatchups(matchups)
                }
            }
        } else {
            loadAllMatchups()
        }
    }

    fun setSortBy(sortBy: SortBy) {
        _uiState.update { it.copy(sortBy = sortBy) }
        applyFilters()
    }

    private fun updateFilteredMatchups(matchups: List<Matchup>) {
        _uiState.update { it.copy(allMatchups = matchups) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.allMatchups

        if (state.searchQuery.isNotBlank()) {
            val query = state.searchQuery.lowercase()
            filtered = filtered.filter { matchup ->
                val yourName = allChampions[matchup.yourChampionId]?.name?.lowercase().orEmpty()
                val enemyName = allChampions[matchup.enemyChampionId]?.name?.lowercase().orEmpty()
                yourName.contains(query) || enemyName.contains(query)
            }
        }

        filtered = when (state.sortBy) {
            SortBy.RECENT -> filtered.sortedByDescending { it.reviewCount }
            SortBy.DIFFICULTY -> filtered.sortedBy {
                when (it.aggregatedDifficulty) {
                    Difficulty.HARD -> 0
                    Difficulty.MEDIUM -> 1
                    Difficulty.EASY -> 2
                    null -> 3
                }
            }
            SortBy.REVIEW_COUNT -> filtered.sortedByDescending { it.reviewCount }
        }

        _uiState.update { it.copy(filteredMatchups = filtered) }
    }

    fun getChampionName(id: String): String = allChampions[id]?.name ?: id
    fun getChampionImageUrl(id: String): String? = allChampions[id]?.imageUrl
}

data class BrowseUiState(
    val searchQuery: String = "",
    val roleFilter: Role? = null,
    val sortBy: SortBy = SortBy.RECENT,
    val allMatchups: List<Matchup> = emptyList(),
    val filteredMatchups: List<Matchup> = emptyList(),
)

enum class SortBy {
    RECENT, DIFFICULTY, REVIEW_COUNT
}
