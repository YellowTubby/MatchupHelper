package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUIState = MatchupScreenUIState()
data class MatchupScreenUIState(
    val matchup: Matchup = Matchup(),
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false, SnackbarMessage()),
    override val loading: Boolean = true,
    override val multiSelectEnabled: Boolean = false,
) : ApplicationUIState()