package com.yellowtubby.victoryvault.model

data class UserData(
    val currentRole: Role = Role.NAN,
    val currentMatchup: Matchup = Matchup(),
    val selectedChampion: Champion = Champion("NAN")
) {
    override fun toString(): String {
        return "(${currentRole.name} , ${currentMatchup.orig.name} -> ${currentMatchup.enemy.name}, ${selectedChampion.name})"
    }
}
