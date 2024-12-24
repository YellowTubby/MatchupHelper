package com.yellowtubby.victoryvault.model

enum class FilterType{
    ROLE,
    NAME,
    AD,
    AP
}
data class MatchupFilter(
    val type: FilterType,
    val filterFunction : (Matchup) -> Boolean
)
