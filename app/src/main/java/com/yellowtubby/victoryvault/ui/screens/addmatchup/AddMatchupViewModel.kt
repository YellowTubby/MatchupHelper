package com.yellowtubby.victoryvault.ui.screens.addmatchup

import androidx.compose.runtime.mutableStateOf
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class AddMatchupViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
)
    : BaseViewModel(sharedFlowProvider,coroutineDispatcher) {

    init {
        collectSharedFlow()
    }

    override suspend fun handleIntent(intent: ApplicationIntent) {
        when(intent) {
            is AddMatchupIntent.RoleChanged -> {
                _uiState.value = _uiState.value.copy(
                    currentRole = intent.newRole,
                )
            }

            is AddMatchupIntent.LocalDataChanged -> {
                _uiState.value = _uiState.value.copy(
                    allChampions = intent.allChampions,
                    selectedChampion = intent.selectedChampion,
                    currentChampion = intent.currentChampion,
                    currentRole = intent.currentRole
                )
            }

            is AddMatchupIntent.CurrentChampionChanged -> {
                _uiState.value = _uiState.value.copy(
                    currentChampion = intent.currentChampion,
                )
            }

            is AddMatchupIntent.AddMatchup -> {
                _intentFlow.emit(
                    MainActivityIntent.LoadingStateChanged(isLoading = true)
                )
                withContext(coroutineDispatcher.io) {
                    val addMatch = async { matchupRepository.addMatchup(intent.matchup) }
                    addMatch.await()
                    val matchups = mutableStateOf(matchupRepository.getAllMatchups())
                    withContext(coroutineDispatcher.ui) {
                        _intentFlow.emit(
                            MainScreenIntent.CurrentChampionMatchupChanged(matchups.value)
                        )
                        _intentFlow.emit(
                            MainActivityIntent.LoadingStateChanged(isLoading = false)
                        )
                    }
                }
            }

            is AddMatchupIntent.SelectedChampion -> {
                _uiState.value = _uiState.value.copy(
                    selectedChampion = intent.champion
                )
            }
        }
    }

    override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is AddMatchupIntent }


    private val _uiState: MutableStateFlow<AddMatchupUiState> = MutableStateFlow(
        ADD_MATCHUP_INIT_STATE
    )

    val uiState: StateFlow<AddMatchupUiState>
        get() = _uiState

}