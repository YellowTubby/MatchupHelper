package com.yellowtubby.victoryvault.ui.screens.addmatchup

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.data.datamodels.Role

sealed class AddMatchupIntent : ApplicationIntent() {
    data class SelectedChampion(val champion: Champion) : AddMatchupIntent()
    data class AddMatchup(val matchup: Matchup) : AddMatchupIntent()
}