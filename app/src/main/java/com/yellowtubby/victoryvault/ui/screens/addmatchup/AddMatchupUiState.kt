package com.yellowtubby.victoryvault.ui.screens.addmatchup

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage

val ADD_MATCHUP_INIT_STATE = AddMatchupUiState(
    currentRole = Role.TOP,
    currentChampion = Champion("Aatrox"),
    allChampions = emptyList(),
    selectedChampion = null,
    selectedDifficulty = 0f
)

data class AddMatchupUiState(
    val currentRole: Role?,
    val currentChampion: Champion?,
    val allChampions: List<Champion>,
    val selectedChampion: Champion?,
    val selectedDifficulty: Float,
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false,SnackbarMessage()),
    override val loading: Boolean = false,
    override val multiSelectEnabled: Boolean = false,
    ) : ApplicationUIState()
