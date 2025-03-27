package com.yellowtubby.victoryvault.feature.matchupdetails.presentation.screen

import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.core.presentation.ApplicationUIState
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage

val MATCHUP_SCREEN_INIT_STATE : MatchupScreenUIState = MatchupScreenUIState()
data class MatchupScreenUIState(
    val matchup: Matchup = Matchup(),
    override var snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false, SnackbarMessage()),
    override val loading: Boolean = true,
    override val multiSelectEnabled: Boolean = false,
) : ApplicationUIState()