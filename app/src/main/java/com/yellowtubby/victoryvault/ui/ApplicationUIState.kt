package com.yellowtubby.victoryvault.ui

import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage

abstract class ApplicationUIState {
    abstract val snackBarMessage: Pair<Boolean, SnackbarMessage>
    abstract val loading: Boolean
    abstract val multiSelectEnabled: Boolean
}