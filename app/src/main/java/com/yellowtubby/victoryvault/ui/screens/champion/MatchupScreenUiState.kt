package com.yellowtubby.victoryvault.ui.screens.champion

import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.screens.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUIState =
    MatchupScreenUIState(
        Matchup()
    )
data class MatchupScreenUIState(
    val matchup: Matchup,
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false,SnackbarMessage()),
    override val loading: Boolean = false
) : ApplicationUIState()