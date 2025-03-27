package com.yellowtubby.victoryvault.app.presentation.screen

import com.yellowtubby.victoryvault.core.presentation.ApplicationUIState
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage


const val BACK_BUTTON_STRING = "back"
const val MENU_BUTTON_STRING = "menu"
const val MENU_EDIT_STRING = "edit"
const val MENU_DELETE_STRING = "delete_selected"

val MAIN_ACTIVITY_STATE = MainActivityUIState()


data class MainActivityUIState(
    val selectedAmount: Int = 0,
    val selectedBottomBarIndex: Int = 1,
    val selectedMatchups: List<Matchup> = listOf(),
    val allChampions: List<Champion> = listOf(),
    val isFabExpanded: Boolean = false,
    val shouldShowFab: Boolean = false,
    val isBottomBarVisible: Boolean = true,
    override var snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false, SnackbarMessage()),
    override val loading: Boolean = false,
    override val multiSelectEnabled: Boolean = false,
) : ApplicationUIState()