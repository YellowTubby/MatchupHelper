package com.yellowtubby.victoryvault.app.presentation.screen

import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent

sealed class MainActivityIntent : ApplicationIntent() {
    data class MultiSelectChanged(val isEnabled: Boolean) : MainActivityIntent()
    data class NavigatedBottomBar(val selectedIndex : Int) : MainActivityIntent()
    data class BottomBarVisibilityChanged(val isVisible: Boolean) : MainActivityIntent()
    data class FabExpandedStateChanged(val isExpanded: Boolean ) : MainActivityIntent()
}