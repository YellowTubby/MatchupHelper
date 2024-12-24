package com.yellowtubby.victoryvault.ui.screens.addmatchup

import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.model.Role

sealed class AddMatchupIntent : ApplicationIntent() {
    data class SelectedChampion(val champion: Champion) : AddMatchupIntent()
    data class AddMatchup(val matchup: Matchup) : AddMatchupIntent()
    data class RoleChanged(val newRole: Role) : AddMatchupIntent()
    data class LocalDataChanged(
        val allChampions : List<Champion>,
        val selectedChampion: Champion,
        val currentChampion: Champion?,
        val currentRole : Role?
    ) : AddMatchupIntent()
    data class CurrentChampionChanged(val currentChampion: Champion?) : AddMatchupIntent()
}