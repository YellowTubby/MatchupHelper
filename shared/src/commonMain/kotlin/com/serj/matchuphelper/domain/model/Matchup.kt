package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Matchup(
    val id: Long,
    val yourChampionId: String,
    val enemyChampionId: String,
    val role: Role,
    val aggregatedDifficulty: Difficulty?,
    val keyTips: List<String>,
    val reviewCount: Int,
)
