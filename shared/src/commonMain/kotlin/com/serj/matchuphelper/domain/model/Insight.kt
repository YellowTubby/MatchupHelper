package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Insight(
    val id: Long,
    val reviewId: Long,
    val matchupId: Long,
    val category: InsightCategory,
    val text: String,
    val gamePhase: GamePhase?,
)
