package com.serj.matchuphelper.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Matchup
import com.serj.matchuphelper.domain.model.MatchupReview
import com.serj.matchuphelper.domain.model.Outcome
import com.serj.matchuphelper.domain.repository.ChampionRepository
import com.serj.matchuphelper.domain.repository.MatchupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var champions = emptyMap<String, Champion>()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                championRepository.syncIfNewPatch()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }

        viewModelScope.launch {
            championRepository.getAllChampions().collect { allChampions ->
                champions = allChampions.associateBy { it.id }
                _uiState.update { it.copy(championCount = allChampions.size) }
            }
        }

        viewModelScope.launch {
            matchupRepository.getRecentReviews(5).collect { reviews ->
                val enriched = reviews.map { review ->
                    RecentReviewItem(
                        review = review,
                        matchupId = review.matchupId,
                    )
                }
                _uiState.update {
                    it.copy(recentReviews = enriched, reviewCount = reviews.size)
                }
            }
        }
    }

    fun getChampionName(id: String): String = champions[id]?.name ?: id
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val championCount: Int = 0,
    val reviewCount: Int = 0,
    val recentReviews: List<RecentReviewItem> = emptyList(),
    val error: String? = null,
)

data class RecentReviewItem(
    val review: MatchupReview,
    val matchupId: Long,
)
