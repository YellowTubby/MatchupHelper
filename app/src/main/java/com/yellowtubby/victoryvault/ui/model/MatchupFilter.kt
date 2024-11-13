package com.yellowtubby.victoryvault.ui.model

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
