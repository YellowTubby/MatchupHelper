package com.yellowtubby.victoryvault.model

data class UserData(
    val currentRole: Role = Role.NAN,
    val currentMatchup: Matchup = Matchup(),
    val selectedChampion: Champion = Champion("NAN")
)
