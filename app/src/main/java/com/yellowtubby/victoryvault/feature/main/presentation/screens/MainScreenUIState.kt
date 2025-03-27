package com.yellowtubby.victoryvault.feature.main.presentation.screens

import com.yellowtubby.victoryvault.data.datamodels.MatchupFilter
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.core.presentation.ApplicationUIState
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage


val MAIN_SCREEN_INIT_STATE : MainScreenUIState = MainScreenUIState(
    definedChampion = listOf(),
    currentChampion = Champion(),
    currentMatchupList = listOf(),
    selectedMatchups = emptyList(),
    currentRole = Role.NAN,
    allChampions = emptyList(),
    filterList = emptyList(),
    loading = true
)
data class MainScreenUIState(
    val definedChampion: List<Champion>,
    val currentChampion: Champion,
    val currentRole : Role,
    val currentMatchupList: List<Matchup>,
    val selectedMatchups: List<Matchup>,
    val allChampions: List<Champion>,
    val filterList : List<MatchupFilter>,
    val textQuery : String = "",
    override var snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false, SnackbarMessage()),
    override val loading: Boolean = false,
    override val multiSelectEnabled: Boolean = false,
) : ApplicationUIState() {

}



