package com.serj.matchuphelper.ui.screen.pool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.PoolEntry
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.ChampionRepository
import com.serj.matchuphelper.domain.repository.MatchupRepository
import com.serj.matchuphelper.domain.repository.PoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PoolViewModel(
    private val poolRepository: PoolRepository,
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PoolUiState())
    val uiState: StateFlow<PoolUiState> = _uiState.asStateFlow()

    private var allChampions = emptyMap<String, Champion>()

    init {
        viewModelScope.launch {
            combine(
                poolRepository.getAllEntries(),
                championRepository.getAllChampions(),
            ) { entries, champions ->
                allChampions = champions.associateBy { it.id }
                entries
            }.collect { entries ->
                val byRole = entries.groupBy { it.role }
                    .mapValues { (_, roleEntries) ->
                        roleEntries.map { entry ->
                            val matchups = matchupRepository
                                .getMatchupsForChampion(entry.championId)
                                .first()
                            PoolChampionItem(
                                entry = entry,
                                champion = allChampions[entry.championId],
                                matchupCount = matchups.size,
                                reviewedMatchupCount = matchups.count { it.reviewCount > 0 },
                            )
                        }
                    }

                _uiState.update {
                    it.copy(
                        entriesByRole = byRole,
                        totalCount = byRole.values.sumOf { items -> items.size },
                    )
                }
            }
        }
    }

    fun searchChampions(query: String) {
        viewModelScope.launch {
            val results = championRepository.searchChampions(query)
            _uiState.update { it.copy(searchResults = results) }
        }
    }

    fun addToPool(champion: Champion, role: Role) {
        viewModelScope.launch {
            poolRepository.addToPool(champion.id, role)
            _uiState.update { it.copy(searchResults = emptyList(), isAddingChampion = false) }
        }
    }

    fun removeFromPool(championId: String, role: Role) {
        viewModelScope.launch {
            poolRepository.removeFromPool(championId, role)
        }
    }

    fun updateComfort(championId: String, role: Role, comfort: Int) {
        viewModelScope.launch {
            poolRepository.updateComfort(championId, role, comfort)
        }
    }

    fun toggleAddMode() {
        _uiState.update { it.copy(isAddingChampion = !it.isAddingChampion, searchResults = emptyList()) }
    }

    fun setAddRole(role: Role) {
        _uiState.update { it.copy(addRole = role) }
    }
}

data class PoolUiState(
    val entriesByRole: Map<Role, List<PoolChampionItem>> = emptyMap(),
    val totalCount: Int = 0,
    val isAddingChampion: Boolean = false,
    val addRole: Role = Role.TOP,
    val searchResults: List<Champion> = emptyList(),
)

data class PoolChampionItem(
    val entry: PoolEntry,
    val champion: Champion?,
    val matchupCount: Int,
    val reviewedMatchupCount: Int = 0,
)
