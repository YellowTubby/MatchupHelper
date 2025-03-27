package com.yellowtubby.victoryvault.feature.addmatchup.presentation

import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.core.presentation.ApplicationUIState
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage

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
    override var snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false, SnackbarMessage()),
    override val loading: Boolean = false,
    override val multiSelectEnabled: Boolean = false,
    ) : ApplicationUIState()
