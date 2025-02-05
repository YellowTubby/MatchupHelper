package com.yellowtubby.victoryvault.ui

import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class MainActivityViewModel : BaseViewModel<MainActivityUIState>() {
    override val _uiState: MutableStateFlow<MainActivityUIState> = MutableStateFlow(
        MAIN_ACTIVITY_STATE
    )
    private val getDefinedChampionsUseCase: ChampionListUseCase by inject(ChampionListUseCase::class.java, qualifier = named("defined"))


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
        definedScope.launch {
            collectSharedFlow()
        }

        definedScope.launch {
            getDefinedChampionsUseCase().collect {
                _uiState.value = _uiState.value.copy(
                    shouldShowFab = it.isNotEmpty()
                )
            }
        }
    }

    override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is MainActivityIntent }


}