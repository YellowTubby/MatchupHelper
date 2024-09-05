package com.yellowtubby.matchuphelper.ui.screens.champion

import com.yellowtubby.matchuphelper.ui.model.Matchup

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUiState =
    MatchupScreenUiState(
        null
    )
data class MatchupScreenUiState(val matchup : Matchup?)