package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.ChampFilter
import com.yellowtubby.matchuphelper.ui.model.Champion

sealed class MatchupIntent {
    data class SelectChampion(val champion: Champion) : MatchupIntent()
    data class StartMultiSelectChampion(val enabled: Boolean) : MatchupIntent()
    data object DeleteSelected: MatchupIntent()
    data class TextFilterChanged(val filter: String) : MatchupIntent()
    data class FilterListChanged(val filter: ChampFilter) : MatchupIntent()
}