package com.serj.matchuphelper.ui.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serj.matchuphelper.data.ai.GeminiMatchupAiService
import com.serj.matchuphelper.db.MatchupDatabase
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
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class ReviewViewModel(
    private val championRepository: ChampionRepository,
    private val matchupRepository: MatchupRepository,
    private val aiService: GeminiMatchupAiService,
    private val database: MatchupDatabase,
    private val json: Json,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private var conversationId: String? = null
    private var draftId: Long? = null

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

                val result = aiService.startReview(context)
                conversationId = result.conversationId

                _uiState.update {
                    it.copy(
                        isAiLoading = false,
                        messages = listOf(result.firstMessage),
                        matchupId = existingMatchup.id,
                    )
                }

                saveDraft()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isAiLoading = false, error = e.message)
                }
            }
        }
    }

    fun resumeDraft(draft: DraftReview) {
        viewModelScope.launch {
            val yourChamp = championRepository.getChampion(draft.yourChampionId)
            val enemyChamp = championRepository.getChampion(draft.enemyChampionId)

            val messages = try {
                json.decodeFromString<List<AiMessage>>(draft.conversationJson)
            } catch (_: Exception) {
                emptyList()
            }

            draftId = draft.id
            conversationId = draft.conversationId

            _uiState.update {
                it.copy(
                    phase = ReviewPhase.CHATTING,
                    yourChampion = yourChamp,
                    enemyChampion = enemyChamp,
                    selectedRole = draft.role,
                    outcome = draft.outcome,
                    messages = messages,
                    exchangeCount = messages.count { msg -> msg.role == MessageRole.USER },
                )
            }
        }
    }

    fun sendMessage(text: String) {
        val convId = conversationId
        if (convId == null) {
            _uiState.update { it.copy(error = "No active conversation. Please start a new review.") }
            return
        }

        _uiState.update {
            it.copy(
                messages = it.messages + AiMessage(MessageRole.USER, text),
                isAiLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            saveDraft()

            try {
                val response = aiService.sendMessage(convId, text)
                _uiState.update {
                    it.copy(
                        messages = it.messages + response,
                        isAiLoading = false,
                        exchangeCount = it.exchangeCount + 1,
                    )
                }
                saveDraft()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isAiLoading = false, error = "AI error: ${e.message}")
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

                deleteDraft()

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

    private fun saveDraft() {
        val state = _uiState.value
        val yourChamp = state.yourChampion ?: return
        val enemyChamp = state.enemyChampion ?: return

        val messagesJson = json.encodeToString(
            kotlinx.serialization.builtins.ListSerializer(AiMessage.serializer()),
            state.messages,
        )

        database.reviewDraftQueries.upsert(
            id = draftId,
            yourChampionId = yourChamp.id,
            enemyChampionId = enemyChamp.id,
            role = state.selectedRole!!.name,
            outcome = state.outcome!!.name,
            conversationJson = messagesJson,
            phase = state.phase.name,
            conversationId = conversationId,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
        )

        if (draftId == null) {
            draftId = database.reviewDraftQueries.selectLastInsertId().executeAsOne()
        }
    }

    private fun deleteDraft() {
        draftId?.let { database.reviewDraftQueries.delete(it) }
        draftId = null
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

data class DraftReview(
    val id: Long,
    val yourChampionId: String,
    val enemyChampionId: String,
    val role: Role,
    val outcome: Outcome,
    val conversationJson: String,
    val conversationId: String?,
)
