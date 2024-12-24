package com.yellowtubby.victoryvault.ui

import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.general.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
) : BaseViewModel<MainActivityUIState>(sharedFlowProvider, coroutineDispatcher) {
    override val _uiState: MutableStateFlow<MainActivityUIState> = MutableStateFlow(
        MAIN_ACTIVITY_STATE
    )

    override suspend fun handleIntent(intent: ApplicationIntent) {
        when(intent){
            is MainActivityIntent.UpdatedSelectedChampions -> {
                _uiState.value = _uiState.value.copy(
                    selectedAmount = intent.championNumber
                )
            }
            is MainActivityIntent.MultiSelectChanged -> {
                _uiState.value = _uiState.value.copy(
                    multiSelectEnabled = intent.isEnabled,
                    selectedAmount = if (!intent.isEnabled) 0 else _uiState.value.selectedAmount
                )
            }

            is MainActivityIntent.NavigatedBottomBar -> {
                _uiState.value = _uiState.value.copy(
                    selectedBottomBarIndex = intent.selectedIndex
                )
            }

            is MainActivityIntent.BottomBarVisibilityChanged -> {
                _uiState.value = _uiState.value.copy(
                    isBottomBarVisible = intent.isVisible
                )
            }

            is MainActivityIntent.FabExpandedStateChanged -> {
                _uiState.value = _uiState.value.copy(
                    isFabExpanded = intent.isExpanded
                )
            }

        }
    }

    init {
        viewModelScope.launch {
            launch {
                collectSharedFlow()
            }
        }
    }

    override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is MainActivityIntent }


}