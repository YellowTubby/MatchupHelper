package com.yellowtubby.victoryvault.ui.screens.champion

import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.ApplicationIntent

sealed class MatchupScreenIntent : ApplicationIntent() {
    data class WinLossChanged(val isWon: Boolean) : MatchupScreenIntent()
}