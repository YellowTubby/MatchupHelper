package com.yellowtubby.victoryvault.ui.screens.add

import com.yellowtubby.victoryvault.ui.model.Champion

val ADD_CHAMP_INIT_STATE = AddChampionUiState(
    null,
    emptyList()
)

data class AddChampionUiState(
    val championSelected: Champion?,
    val allChampions: List<Champion>,
)
