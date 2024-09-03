package com.yellowtubby.matchuphelper.ui.screens.add

import com.yellowtubby.matchuphelper.ui.model.Champion
import com.yellowtubby.matchuphelper.ui.screens.ApplicationIntent

sealed class AddChampionIntent : ApplicationIntent() {
    data class ChampionSelected(val champion: Champion) : AddChampionIntent()
    data class AddChampion(val champion: Champion) : AddChampionIntent()

}