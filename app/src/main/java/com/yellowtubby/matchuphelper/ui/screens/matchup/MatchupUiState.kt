package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.MatchupFilter
import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Matchup
import com.yellowtubby.matchuphelper.ui.model.Role


val MATCH_SCREEN_INIT_STATE = MatchupUiState(
    definedChampion = listOf(),
    currentChampion = null,
    matchupsForCurrentChampion = listOf(),
    isInMultiSelect = false,
    selectedChampions = emptyList(),
    currentRole = null,
    filterList = emptyList()
)
data class MatchupUiState(
    val definedChampion: List<Champion>,
    val currentChampion: Champion?,
    val currentRole : Role?,
    val matchupsForCurrentChampion: List<Matchup>,
    val isInMultiSelect : Boolean,
    val selectedChampions: List<Champion>,
    val filterList : List<MatchupFilter>,
    val textQuery : String = "",
) {
}


