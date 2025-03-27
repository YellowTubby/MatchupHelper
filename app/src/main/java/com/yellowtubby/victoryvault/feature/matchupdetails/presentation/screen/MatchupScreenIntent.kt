package com.yellowtubby.victoryvault.feature.matchupdetails.presentation.screen

import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup

sealed class MatchupScreenIntent : ApplicationIntent() {
    data class WinLossChanged(val isWon: Boolean) : MatchupScreenIntent()
    data class LoadMatchupInfo(val matchup: Matchup, val enemy: Champion) : MatchupScreenIntent()
    data object ErrorClear : MatchupScreenIntent()
}