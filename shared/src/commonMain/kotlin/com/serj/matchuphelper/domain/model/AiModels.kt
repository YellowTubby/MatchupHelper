package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AiMessage(
    val role: MessageRole,
    val content: String,
)

@Serializable
enum class MessageRole {
    USER, ASSISTANT
}

data class ReviewContext(
    val yourChampion: Champion,
    val enemyChampion: Champion,
    val role: Role,
    val outcome: Outcome,
    val existingInsights: List<Insight>,
)
