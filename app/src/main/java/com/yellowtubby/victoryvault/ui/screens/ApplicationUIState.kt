package com.yellowtubby.victoryvault.ui.screens

import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage

abstract class ApplicationUIState{
    abstract val snackBarMessage : Pair<Boolean, SnackbarMessage>
    abstract val loading : Boolean
}
