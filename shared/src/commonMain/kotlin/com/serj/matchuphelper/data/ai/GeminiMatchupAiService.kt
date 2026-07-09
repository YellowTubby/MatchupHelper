package com.serj.matchuphelper.data.ai

import com.serj.matchuphelper.domain.model.AiMessage
import com.serj.matchuphelper.domain.model.Difficulty
import com.serj.matchuphelper.domain.model.GamePhase
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.InsightCategory
import com.serj.matchuphelper.domain.model.MessageRole
import com.serj.matchuphelper.domain.model.ReviewContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GeminiMatchupAiService(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val json: Json,
) : MatchupAiService {

    private val conversations = mutableMapOf<String, ConversationState>()
    private var nextConversationId = 0L

    override suspend fun startReview(context: ReviewContext): StartReviewResult {
        val conversationId = (nextConversationId++).toString()
        val systemPrompt = MatchupPrompts.buildSystemPrompt(context)

        val userContent = GeminiContent(
            role = "user",
            parts = listOf(GeminiPart("I just finished a game. Ready for the review.")),
        )

        val request = GeminiRequest(
            contents = listOf(userContent),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(systemPrompt))),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                maxOutputTokens = 300,
            ),
        )

        val response = callGemini(request)
        val responseText = response.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: throw IllegalStateException(
                response.error?.message ?: "Empty response from Gemini"
            )

        val state = ConversationState(
            context = context,
            systemPrompt = systemPrompt,
            messages = mutableListOf(
                userContent,
                GeminiContent(role = "model", parts = listOf(GeminiPart(responseText))),
            ),
        )
        conversations[conversationId] = state

        return StartReviewResult(
            conversationId = conversationId,
            firstMessage = AiMessage(role = MessageRole.ASSISTANT, content = responseText),
        )
    }

    override suspend fun sendMessage(conversationId: String, userMessage: String): AiMessage {
        val state = conversations[conversationId]
            ?: throw IllegalArgumentException("Conversation $conversationId not found")

        state.messages.add(
            GeminiContent(role = "user", parts = listOf(GeminiPart(userMessage)))
        )

        val request = GeminiRequest(
            contents = state.messages.toList(),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(state.systemPrompt))),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                maxOutputTokens = 300,
            ),
        )

        val response = callGemini(request)
        val responseText = response.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: throw IllegalStateException(
                response.error?.message ?: "Empty response from Gemini"
            )

        state.messages.add(
            GeminiContent(role = "model", parts = listOf(GeminiPart(responseText)))
        )

        return AiMessage(role = MessageRole.ASSISTANT, content = responseText)
    }

    override suspend fun extractInsights(conversationId: String): List<Insight> {
        val state = conversations[conversationId]
            ?: throw IllegalArgumentException("Conversation $conversationId not found")

        val transcript = state.messages.joinToString("\n\n") { content ->
            val role = if (content.role == "model") "Coach" else "Player"
            "$role: ${content.parts.firstOrNull()?.text.orEmpty()}"
        }

        val extractionPrompt = MatchupPrompts.buildExtractionPrompt(state.context, transcript)

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(extractionPrompt)),
                )
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.1f,
                maxOutputTokens = 1000,
            ),
        )

        val response = callGemini(request)
        val responseText = response.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: throw IllegalStateException("Failed to extract insights")

        return parseInsightsResponse(responseText)
    }

    fun getConversationTranscript(conversationId: String): String {
        val state = conversations[conversationId] ?: return "[]"
        return json.encodeToString(
            kotlinx.serialization.builtins.ListSerializer(AiMessage.serializer()),
            state.messages.map { content ->
                AiMessage(
                    role = if (content.role == "model") MessageRole.ASSISTANT else MessageRole.USER,
                    content = content.parts.firstOrNull()?.text.orEmpty(),
                )
            }
        )
    }

    fun getExtractedDifficulty(conversationId: String): Difficulty {
        return conversations[conversationId]?.extractedDifficulty ?: Difficulty.MEDIUM
    }

    private suspend fun callGemini(request: GeminiRequest): GeminiResponse {
        return httpClient.post(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    private fun parseInsightsResponse(responseText: String): List<Insight> {
        val jsonStart = responseText.indexOf('{')
        val jsonEnd = responseText.lastIndexOf('}')
        if (jsonStart == -1 || jsonEnd == -1 || jsonEnd <= jsonStart) {
            return emptyList()
        }
        val cleanJson = responseText.substring(jsonStart, jsonEnd + 1)

        val parsed = json.decodeFromString<ExtractionResponse>(cleanJson)

        conversations.values.lastOrNull()?.extractedDifficulty =
            try { Difficulty.valueOf(parsed.difficulty.uppercase()) } catch (_: Exception) { Difficulty.MEDIUM }

        return parsed.insights.mapNotNull { dto ->
            val category = try { InsightCategory.valueOf(dto.category.uppercase()) } catch (_: Exception) { null }
                ?: return@mapNotNull null
            Insight(
                id = 0,
                reviewId = 0,
                matchupId = 0,
                category = category,
                text = dto.text,
                gamePhase = dto.gamePhase?.let {
                    try { GamePhase.valueOf(it.uppercase()) } catch (_: Exception) { null }
                },
            )
        }
    }

    private data class ConversationState(
        val context: ReviewContext,
        val systemPrompt: String,
        val messages: MutableList<GeminiContent>,
        var extractedDifficulty: Difficulty? = null,
    )
}

@Serializable
private data class ExtractionResponse(
    val difficulty: String,
    val insights: List<InsightDto>,
)

@Serializable
private data class InsightDto(
    val category: String,
    val text: String,
    val gamePhase: String? = null,
)
