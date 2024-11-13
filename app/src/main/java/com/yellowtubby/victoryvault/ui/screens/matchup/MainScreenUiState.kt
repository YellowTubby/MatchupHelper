package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.ui.model.MatchupFilter
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.Role


val MAIN_SCREEN_INIT_STATE = MainScreenUiState(
    definedChampion = listOf(),
    currentChampion = null,
    matchupsForCurrentChampion = listOf(),
    isInMultiSelect = false,
    selectedMatchups = emptyList(),
    currentRole = null,
    filterList = emptyList()
)
data class MainScreenUiState(
    val definedChampion: List<Champion>,
    val currentChampion: Champion?,
    val currentRole : Role?,
    val matchupsForCurrentChampion: List<Matchup>,
    val isInMultiSelect : Boolean,
    val selectedMatchups: List<Matchup>,
    val filterList : List<MatchupFilter>,
    val textQuery : String = "",
)

