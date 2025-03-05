package com.yellowtubby.victoryvault.data.datamodels

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
