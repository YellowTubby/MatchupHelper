package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.ChampFilter
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Role
import com.yellowtubby.matchuphelper.ui.screens.ApplicationIntent

sealed class MatchupIntent : ApplicationIntent() {
    data class MultiSelectChampion(val champion: Champion) : MatchupIntent()
    data class SelectChampion(val champion: Champion) : MatchupIntent()
    data class StartMultiSelectChampion(val enabled: Boolean) : MatchupIntent()
    data object DeleteSelected: MatchupIntent()
    data class TextFilterChanged(val filter: String) : MatchupIntent()
    data class FilterListChanged(val filter: ChampFilter) : MatchupIntent()
    data class RoleChanged(val role: Role): MatchupIntent()
    data object LoadLocalData: MatchupIntent()
}