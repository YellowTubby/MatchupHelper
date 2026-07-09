package com.serj.matchuphelper.data.ai

import com.serj.matchuphelper.domain.model.AiMessage
import com.serj.matchuphelper.domain.model.Insight
import com.serj.matchuphelper.domain.model.ReviewContext

interface MatchupAiService {
    suspend fun startReview(context: ReviewContext): AiMessage
    suspend fun sendMessage(conversationId: String, userMessage: String): AiMessage
    suspend fun extractInsights(conversationId: String): List<Insight>
}
