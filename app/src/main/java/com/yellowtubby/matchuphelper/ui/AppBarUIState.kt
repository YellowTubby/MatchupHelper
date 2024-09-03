package com.yellowtubby.matchuphelper.ui


const val BACK_BUTTON_STRING = "back"
const val MENU_BUTTON_STRING = "menu"
const val MENU_DELETE_STRING = "delete_selected"

val APP_BAR_INIT_STATE = AppBarUIState(
    false,
    0
)


data class AppBarUIState(
    val isInMultiSelect: Boolean,
    val selectedAmount: Int
)