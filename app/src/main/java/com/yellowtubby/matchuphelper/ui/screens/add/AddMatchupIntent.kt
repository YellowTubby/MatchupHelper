package com.yellowtubby.matchuphelper.ui.screens.add

import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.screens.ApplicationIntent

sealed class AddMatchupIntent : ApplicationIntent() {
    data class SelectedChampion(val champion: Champion) : AddMatchupIntent()
    data class AddMatchup(val matchup: Matchup) : AddMatchupIntent()
}