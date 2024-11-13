package com.yellowtubby.victoryvault.ui.screens.champion

import com.yellowtubby.victoryvault.ui.model.Matchup

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUiState =
    MatchupScreenUiState(
        Matchup()
    )
data class MatchupScreenUiState(val matchup : Matchup)