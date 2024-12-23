package com.yellowtubby.victoryvault.ui.screens.main

import com.yellowtubby.victoryvault.ui.model.MatchupFilter
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.Role
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage


val MAIN_SCREEN_INIT_STATE : MainScreenUIState = MainScreenUIState(
    definedChampion = listOf(),
    currentChampion = null,
    matchupsForCurrentChampion = listOf(),
    isInMultiSelect = false,
    selectedMatchups = emptyList(),
    currentRole = null,
    allChampions = emptyList(),
    filterList = emptyList()
)
data class MainScreenUIState(
    val definedChampion: List<Champion>,
    val currentChampion: Champion?,
    val currentRole : Role?,
    val matchupsForCurrentChampion: List<Matchup>,
    val isInMultiSelect : Boolean,
    val selectedMatchups: List<Matchup>,
    val allChampions: List<Champion>,
    val filterList : List<MatchupFilter>,
    val textQuery : String = "",
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false,SnackbarMessage()),
) : ApplicationUIState()


