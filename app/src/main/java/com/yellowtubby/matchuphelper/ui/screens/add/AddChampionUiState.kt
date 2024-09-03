package com.yellowtubby.matchuphelper.ui.screens.add

import com.yellowtubby.matchuphelper.ui.model.Champion

val ADD_CHAMP_INIT_STATE = AddChampionUiState(
    null,
    emptyList()
)

data class AddChampionUiState(
    val championSelected: Champion?,
    val allChampions: List<Champion>,
)
