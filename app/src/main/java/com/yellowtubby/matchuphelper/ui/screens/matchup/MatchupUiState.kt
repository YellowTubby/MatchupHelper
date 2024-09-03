package com.yellowtubby.matchuphelper.ui.screens.matchup

import com.yellowtubby.matchuphelper.ui.model.ChampFilter
import com.yellowtubby.matchuphelper.ui.model.Champion
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
    val matchupsForCurrentChampion: List<Champion>,
    val isInMultiSelect : Boolean,
    val selectedChampions: List<Champion>,
    val filterList : List<ChampFilter>,
    val textQuery : String = "",
) {
}


