package com.yellowtubby.matchuphelper.ui.screens.champion

import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.screens.ApplicationIntent

sealed class MatchupScreenIntent : ApplicationIntent() {
    data class LoadMatchup(val matchup: Matchup) : MatchupScreenIntent()
    data class WinLossChanged(val isWon: Boolean) : MatchupScreenIntent()
}