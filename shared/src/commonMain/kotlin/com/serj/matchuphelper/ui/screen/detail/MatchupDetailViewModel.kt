package com.serj.matchuphelper.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.GamePhase
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.InsightCategory
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

class MatchupDetailViewModel(
    private val matchupId: Long,
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchupDetailUiState())
    val uiState: StateFlow<MatchupDetailUiState> = _uiState.asStateFlow()

    init {
        loadMatchupData()
    }

    private fun loadMatchupData() {
        viewModelScope.launch {
            try {
                val reviews = matchupRepository.getReviewsForMatchup(matchupId)
                val insights = matchupRepository.getInsightsForMatchup(matchupId)

                // Group insights by category
                val groupedInsights = insights
                    .groupBy { it.category }
                    .mapValues { (_, categoryInsights) ->
                        categoryInsights.distinctBy { it.text }
                    }

                val wins = reviews.count { it.outcome == Outcome.WIN }
                val losses = reviews.count { it.outcome == Outcome.LOSS }

                // Determine which matchup this is
                matchupRepository.getMatchupsForChampion("").collect { _ -> }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        reviews = reviews,
                        insightsByCategory = groupedInsights,
                        wins = wins,
                        losses = losses,
                        reviewCount = reviews.size,
                    )
                }

                // Load champion info
                // We need a way to get the matchup itself
                loadMatchupInfo()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private suspend fun loadMatchupInfo() {
        // Get all matchups and find ours
        val reviews = matchupRepository.getReviewsForMatchup(matchupId)
        val insights = matchupRepository.getInsightsForMatchup(matchupId)

        val groupedInsights = insights
            .groupBy { it.category }
            .mapValues { (_, categoryInsights) ->
                categoryInsights.distinctBy { it.text }
            }

        val wins = reviews.count { it.outcome == Outcome.WIN }
        val losses = reviews.count { it.outcome == Outcome.LOSS }

        // Compute most common difficulty
        val difficulty = reviews
            .groupBy { it.difficultyRating }
            .maxByOrNull { it.value.size }
            ?.key

        _uiState.update {
            it.copy(
                isLoading = false,
                reviews = reviews,
                insightsByCategory = groupedInsights,
                wins = wins,
                losses = losses,
                reviewCount = reviews.size,
                aggregatedDifficulty = difficulty,
            )
        }
    }

    fun setChampionNames(yourChampName: String, enemyChampName: String) {
        _uiState.update {
            it.copy(yourChampionName = yourChampName, enemyChampionName = enemyChampName)
        }
    }
}

data class MatchupDetailUiState(
    val isLoading: Boolean = true,
    val yourChampionName: String = "",
    val enemyChampionName: String = "",
    val aggregatedDifficulty: Difficulty? = null,
    val wins: Int = 0,
    val losses: Int = 0,
    val reviewCount: Int = 0,
    val insightsByCategory: Map<InsightCategory, List<Insight>> = emptyMap(),
    val reviews: List<MatchupReview> = emptyList(),
    val error: String? = null,
)
