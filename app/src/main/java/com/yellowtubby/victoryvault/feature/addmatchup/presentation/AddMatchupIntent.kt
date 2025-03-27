package com.yellowtubby.victoryvault.feature.addmatchup.presentation

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent

sealed class AddMatchupIntent : ApplicationIntent() {
    data class SelectedChampion(val champion: Champion) : AddMatchupIntent()
    data class AddMatchup(val matchup: Matchup) : AddMatchupIntent()
}