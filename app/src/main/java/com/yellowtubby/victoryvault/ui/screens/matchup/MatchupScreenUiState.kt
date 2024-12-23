package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUIState =
    MatchupScreenUIState(
        Matchup()
    )
data class MatchupScreenUIState(
    val matchup: Matchup,
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false,SnackbarMessage()),
) : ApplicationUIState()