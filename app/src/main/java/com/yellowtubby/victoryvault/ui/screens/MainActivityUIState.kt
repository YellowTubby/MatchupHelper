package com.yellowtubby.victoryvault.ui.screens


const val BACK_BUTTON_STRING = "back"
const val MENU_BUTTON_STRING = "menu"
const val MENU_DELETE_STRING = "delete_selected"

val MAIN_ACTIVITY_STATE = MainActivityUIState(
    false,
    0,
    false,
     1,
    true
)


data class MainActivityUIState(
    val isInMultiSelect: Boolean,
    val selectedAmount: Int,
    val loading: Boolean = false,
    val selectedBottomBarIndex: Int,
    val isBottomBarVisible: Boolean
)