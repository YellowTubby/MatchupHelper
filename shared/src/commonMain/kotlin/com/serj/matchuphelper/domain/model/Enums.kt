package com.serj.matchuphelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    TOP, JUNGLE, MID, BOT, SUPPORT
}

@Serializable
enum class Difficulty {
    EASY, MEDIUM, HARD
}

@Serializable
enum class Outcome {
    WIN, LOSS
}

@Serializable
enum class GamePhase {
    EARLY, MID, LATE
}

@Serializable
enum class InsightCategory {
    LANING, TRADING, POWER_SPIKE, ITEMIZATION, MACRO, RUNES, SUMMONER_SPELLS
}
