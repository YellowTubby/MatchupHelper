package com.yellowtubby.victoryvault.ui.screens.add

import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.screens.ApplicationIntent

sealed class AddChampionIntent : ApplicationIntent() {
    data class AddChampion(val champion: Champion) : AddChampionIntent()
}