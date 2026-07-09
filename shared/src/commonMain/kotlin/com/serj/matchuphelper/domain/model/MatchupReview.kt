package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchupReview(
    val id: Long,
    val matchupId: Long,
    val timestampEpochMillis: Long,
    val outcome: Outcome,
    val difficultyRating: Difficulty,
    val conversationJson: String,
    val personalNotes: String,
)
