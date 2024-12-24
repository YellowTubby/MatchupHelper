package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup

sealed class MatchupScreenIntent : ApplicationIntent() {
    data class WinLossChanged(val isWon: Boolean) : MatchupScreenIntent()
    data class LoadMatchupInfo(val matchup: Matchup, val enemy: Champion) : MatchupScreenIntent()
    data object ErrorClear : MatchupScreenIntent()
}