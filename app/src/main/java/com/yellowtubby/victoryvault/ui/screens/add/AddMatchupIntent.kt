package com.yellowtubby.victoryvault.ui.screens.add

import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.ApplicationIntent

sealed class AddMatchupIntent : ApplicationIntent() {
    data class SelectedChampion(val champion: Champion) : AddMatchupIntent()
    data class AddMatchup(val matchup: Matchup) : AddMatchupIntent()
}