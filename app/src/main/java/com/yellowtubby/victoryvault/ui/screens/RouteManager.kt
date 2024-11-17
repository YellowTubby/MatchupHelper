package com.yellowtubby.victoryvault.ui.screens

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object MatchupInfo : Route("matchupInfo")
    data object AddChampion : Route("addChampion")
    data object AddMatchup : Route("addMatchup")
    data object MyProfile : Route("profile")
    data object Statistics : Route("statistics")

}