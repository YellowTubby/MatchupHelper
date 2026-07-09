package com.serj.matchuphelper.ui.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.data.ai.GeminiMatchupAiService
import com.serj.matchuphelper.domain.model.AiMessage
import com.serj.matchuphelper.domain.model.Champion
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.MatchupReview
import com.serj.matchuphelper.domain.model.MessageRole
import com.serj.matchuphelper.domain.model.Outcome
import com.serj.matchuphelper.domain.model.ReviewContext
import com.serj.matchuphelper.domain.model.Role
import com.serj.matchuphelper.domain.repository.ChampionRepository
import com.serj.matchuphelper.domain.repository.MatchupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ReviewViewModel(
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
    private val aiService: GeminiMatchupAiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private var conversationId: String? = null

    fun searchChampions(query: String) {
        viewModelScope.launch {
            val results = championRepository.searchChampions(query)
            _uiState.update { it.copy(searchResults = results) }
        }
    }

    fun selectYourChampion(champion: Champion) {
        _uiState.update { it.copy(yourChampion = champion, searchResults = emptyList()) }
    }

    fun selectEnemyChampion(champion: Champion) {
        _uiState.update { it.copy(enemyChampion = champion, searchResults = emptyList()) }
    }

    fun selectRole(role: Role) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun selectOutcome(outcome: Outcome) {
        _uiState.update { it.copy(outcome = outcome) }
    }

    fun canStartReview(): Boolean {
        val state = _uiState.value
        return state.yourChampion != null
            && state.enemyChampion != null
            && state.selectedRole != null
            && state.outcome != null
    }

    fun startReview() {
        val state = _uiState.value
        val yourChamp = state.yourChampion ?: return
        val enemyChamp = state.enemyChampion ?: return
        val role = state.selectedRole ?: return
        val outcome = state.outcome ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(phase = ReviewPhase.CHATTING, isAiLoading = true) }

            try {
                val existingMatchup = matchupRepository.getOrCreateMatchup(
                    yourChamp.id, enemyChamp.id, role
                )
                val existingInsights = matchupRepository.getInsightsForMatchup(existingMatchup.id)

                val context = ReviewContext(
                    yourChampion = yourChamp,
                    enemyChampion = enemyChamp,
                    role = role,
                    outcome = outcome,
                    existingInsights = existingInsights,
                )

                val firstMessage = aiService.startReview(context)
                conversationId = "0"

                _uiState.update {
                    it.copy(
                        isAiLoading = false,
                        messages = listOf(firstMessage),
                        matchupId = existingMatchup.id,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isAiLoading = false, error = e.message)
                }
            }
        }
    }

    fun sendMessage(text: String) {
        val convId = conversationId ?: return

        _uiState.update {
            it.copy(
                messages = it.messages + AiMessage(MessageRole.USER, text),
                isAiLoading = true,
            )
        }

        viewModelScope.launch {
            try {
                val response = aiService.sendMessage(convId, text)
                _uiState.update {
                    it.copy(
                        messages = it.messages + response,
                        isAiLoading = false,
                        exchangeCount = it.exchangeCount + 1,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isAiLoading = false, error = e.message)
                }
            }
        }
    }

    fun finishReview() {
        val convId = conversationId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(phase = ReviewPhase.EXTRACTING, isAiLoading = true) }

            try {
                val insights = aiService.extractInsights(convId)
                val difficulty = aiService.getExtractedDifficulty(convId)

                _uiState.update {
                    it.copy(
                        phase = ReviewPhase.SUMMARY,
                        isAiLoading = false,
                        extractedInsights = insights,
                        extractedDifficulty = difficulty,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        phase = ReviewPhase.SUMMARY,
                        isAiLoading = false,
                        error = "Failed to extract insights: ${e.message}",
                        extractedInsights = emptyList(),
                        extractedDifficulty = Difficulty.MEDIUM,
                    )
                }
            }
        }
    }

    fun removeInsight(index: Int) {
        _uiState.update {
            it.copy(extractedInsights = it.extractedInsights.toMutableList().apply { removeAt(index) })
        }
    }

    fun updatePersonalNotes(notes: String) {
        _uiState.update { it.copy(personalNotes = notes) }
    }

    fun saveReview() {
        val state = _uiState.value
        val convId = conversationId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            try {
                val conversationJson = aiService.getConversationTranscript(convId)

                val review = MatchupReview(
                    id = 0,
                    matchupId = state.matchupId,
                    timestampEpochMillis = Clock.System.now().toEpochMilliseconds(),
                    outcome = state.outcome!!,
                    difficultyRating = state.extractedDifficulty ?: Difficulty.MEDIUM,
                    conversationJson = conversationJson,
                    personalNotes = state.personalNotes,
                )

                val reviewId = matchupRepository.saveReview(review)

                val insightsWithIds = state.extractedInsights.map { insight ->
                    insight.copy(reviewId = reviewId, matchupId = state.matchupId)
                }
                matchupRepository.saveInsights(insightsWithIds)

                _uiState.update {
                    it.copy(phase = ReviewPhase.SAVED, isSaving = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, error = e.message)
                }
            }
        }
    }
}

data class ReviewUiState(
    val phase: ReviewPhase = ReviewPhase.SETUP,
    val yourChampion: Champion? = null,
    val enemyChampion: Champion? = null,
    val selectedRole: Role? = null,
    val outcome: Outcome? = null,
    val searchResults: List<Champion> = emptyList(),
    val messages: List<AiMessage> = emptyList(),
    val isAiLoading: Boolean = false,
    val isSaving: Boolean = false,
    val exchangeCount: Int = 0,
    val matchupId: Long = 0,
    val extractedInsights: List<Insight> = emptyList(),
    val extractedDifficulty: Difficulty? = null,
    val personalNotes: String = "",
    val error: String? = null,
)

enum class ReviewPhase {
    SETUP, CHATTING, EXTRACTING, SUMMARY, SAVED
}
