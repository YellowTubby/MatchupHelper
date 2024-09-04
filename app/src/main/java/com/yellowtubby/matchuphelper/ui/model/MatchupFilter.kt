package com.yellowtubby.matchuphelper.ui.model

data class MatchupFilter(
    val filterFunction : (Matchup) -> Boolean
)
