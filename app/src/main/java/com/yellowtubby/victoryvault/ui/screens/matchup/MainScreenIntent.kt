package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.ui.model.MatchupFilter
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.Role
import com.yellowtubby.victoryvault.ui.screens.ApplicationIntent

sealed class MainScreenIntent : ApplicationIntent() {
    data class MultiSelectMatchups(val matchup: Matchup) : MainScreenIntent()
    data class SelectChampion(val champion: Champion) : MainScreenIntent()
    data class StartMultiSelectChampion(val enabled: Boolean) : MainScreenIntent()
    data class DeleteSelected(val selectedMatchupsToDelete : List<Matchup>): MainScreenIntent()
    data class TextFilterChanged(val filter: String) : MainScreenIntent()
    data class FilterListChanged(val filter: MatchupFilter) : MainScreenIntent()
    data class RoleChanged(val role: Role): MainScreenIntent()
    data class SelectedMatchup(val matchup: Matchup) : MainScreenIntent()
    data class NavigatedBottomBar(val selectedIndex: Int) : MainScreenIntent()
    data class LoadMatchupInfo(val matchup: Matchup) : MainScreenIntent()


    data object LoadLocalData: MainScreenIntent()
}