package com.yellowtubby.matchuphelper.ui.screens.add

import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.model.Role

val ADD_MATCHUP_INIT_STATE = AddMatchupUiState(
    currentRole = Role.TOP,
    currentChampion = Champion("Aatrox"),
    allChampions = emptyList()
)

data class AddMatchupUiState(
    val currentRole : Role,
    val currentChampion: Champion,
    val allChampions: List<Champion>
)
