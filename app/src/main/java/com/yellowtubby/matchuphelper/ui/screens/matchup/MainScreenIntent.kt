package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.MatchupFilter
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.model.Role
import com.yellowtubby.matchuphelper.ui.screens.ApplicationIntent

sealed class MainScreenIntent : ApplicationIntent() {
    data class MultiSelectMatchups(val matchup: Matchup) : MainScreenIntent()
    data class SelectChampion(val champion: Champion) : MainScreenIntent()
    data class StartMultiSelectChampion(val enabled: Boolean) : MainScreenIntent()
    data class DeleteSelected(val selectedMatchupsToDelete : List<Matchup>): MainScreenIntent()
    data class TextFilterChanged(val filter: String) : MainScreenIntent()
    data class FilterListChanged(val filter: MatchupFilter) : MainScreenIntent()
    data class RoleChanged(val role: Role): MainScreenIntent()
    data class SelectedMatchup(val matchup: Matchup) : MainScreenIntent()

    data object LoadLocalData: MainScreenIntent()
}