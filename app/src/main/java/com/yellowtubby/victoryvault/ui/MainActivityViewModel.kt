package com.yellowtubby.victoryvault.ui

import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.matchups.AddMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class MainActivityViewModel : BaseViewModel<MainActivityUIState>() {
    override val _uiState: MutableStateFlow<MainActivityUIState> = MutableStateFlow(
        MAIN_ACTIVITY_STATE
    )
    private val getDefinedChampionsUseCase: ChampionListUseCase by inject(ChampionListUseCase::class.java, qualifier = named("defined"))
    private val getMultiSelectedMatchupsUseCase : GetMultiSelectedMatchupsUseCase by inject(GetMultiSelectedMatchupsUseCase::class.java)
    private val addMultiSelectedMatchupsUseCase : AddMultiSelectedMatchupsUseCase by inject(AddMultiSelectedMatchupsUseCase::class.java)
    private val removeMultiSelectedMatchupsUseCase : RemoveMultiSelectedMatchupsUseCase by inject(RemoveMultiSelectedMatchupsUseCase::class.java)

    override suspend fun handleIntent(intent: ApplicationIntent) {
        when(intent){
            is MainActivityIntent.MultiSelectChanged -> {
                if (!intent.isEnabled) {
                    removeMultiSelectedMatchupsUseCase()
                } else {
                    addMultiSelectedMatchupsUseCase()
                }
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
            getMultiSelectedMatchupsUseCase().collect {
                _uiState.value = _uiState.value.copy(
                    selectedAmount = it.second.size,
                    multiSelectEnabled = it.first
                )
            }
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