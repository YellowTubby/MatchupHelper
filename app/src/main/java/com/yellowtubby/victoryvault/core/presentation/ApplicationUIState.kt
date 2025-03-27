package com.yellowtubby.victoryvault.core.presentation

import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage

abstract class ApplicationUIState {
    abstract var snackBarMessage: Pair<Boolean, SnackbarMessage>
    abstract val loading: Boolean
    abstract val multiSelectEnabled: Boolean
}