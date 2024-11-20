package com.yellowtubby.victoryvault.ui.screens

import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage


const val BACK_BUTTON_STRING = "back"
const val MENU_BUTTON_STRING = "menu"
const val MENU_EDIT_STRING = "edit"
const val MENU_DELETE_STRING = "delete_selected"

val MAIN_ACTIVITY_STATE = MainActivityUIState()


data class MainActivityUIState(
    val isInMultiSelect: Boolean = false,
    val selectedAmount: Int = 0,
    val selectedBottomBarIndex: Int = 1,
    val isFabExpanded: Boolean = false,
    val isBottomBarVisible: Boolean = true,
    override val snackBarMessage: Pair<Boolean, SnackbarMessage> = Pair(false,SnackbarMessage()),
    override val loading: Boolean = false
) : ApplicationUIState()