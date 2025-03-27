package com.yellowtubby.victoryvault.feature.main.presentation.screens

import com.yellowtubby.victoryvault.data.datamodels.MatchupFilter
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent

sealed class MainScreenIntent : ApplicationIntent() {
    data class MultiSelectMatchups(val matchup: Matchup) : MainScreenIntent()
    data class SelectChampion(val champion: Champion) : MainScreenIntent()
    data class StartMultiSelectChampion(val enabled: Boolean) : MainScreenIntent()
    data class DeleteSelected(val selectedMatchupsToDelete : List<Matchup>): MainScreenIntent()
    data class TextFilterChanged(val filter: String) : MainScreenIntent()
    data class FilterListChanged(val filter: MatchupFilter) : MainScreenIntent()
    data class RoleChanged(val role: Role): MainScreenIntent()
    data class SelectedMatchup(val matchup: Matchup) : MainScreenIntent()
    data class AddChampion(val champion: Champion) : MainScreenIntent()
    data class CurrentChampionMatchupChanged(val matchupList: List<Matchup>) : MainScreenIntent()
    data object ErrorClear: MainScreenIntent()
}