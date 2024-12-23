package com.yellowtubby.victoryvault.ui.screens.matchup

import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class MatchupViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
) : BaseViewModel(sharedFlowProvider, coroutineDispatcher) {

    init {
        collectSharedFlow()
    }
    override suspend fun handleIntent(intent: ApplicationIntent) {
        when (intent) {
            is MatchupScreenIntent.WinLossChanged -> {
                withContext(coroutineDispatcher.ui) {
                    var prevMatch = _uiState.value.matchup
                    prevMatch = prevMatch.copy(
                        numTotal = prevMatch.numTotal + 1
                    )
                    if (intent.isWon) {
                        prevMatch = prevMatch.copy(
                            numWins = prevMatch.numWins + 1
                        )
                    }
                    withContext(coroutineDispatcher.io) {
                        matchupRepository.updateMatchup(prevMatch)
                    }
                    withContext(coroutineDispatcher.io){
                        val mutableList: MutableList<Matchup> = matchupRepository.getAllMatchups().toMutableList()
                        mutableList.removeIf {
                            it.orig == prevMatch.orig && it.enemy == prevMatch.enemy
                        }
                        mutableList.add(prevMatch)
                        _uiState.value = _uiState.value.copy(
                            matchup = mutableList.first {
                                prevMatch.orig == it.orig && prevMatch.enemy == it.enemy
                            }
                        )
                    }
                }
            }

            is MatchupScreenIntent.LoadMatchupInfo -> {
                _intentFlow.emit(MainActivityIntent.LoadingStateChanged(isLoading = true))
                withContext(coroutineDispatcher.io){
                    val filterMatchup = intent.matchup
                    _uiState.value = _uiState.value.copy(
                        matchup = matchupRepository.getAllMatchups().first {
                            it.enemy == filterMatchup.enemy && it.orig == filterMatchup.orig
                        }
                    )
                }
                _intentFlow.emit(MainActivityIntent.LoadingStateChanged(isLoading = false))
            }

            is MatchupScreenIntent.ErrorClear -> {
                _uiState.value = _uiState.value.copy(
                    snackBarMessage = Pair(false, SnackbarMessage())
                )
            }
        }
    }

    override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is MatchupScreenIntent }

    private val _uiState = MutableStateFlow(
        MATCHUP_SCREEN_INIT_STATE
    )
    val uiState: StateFlow<MatchupScreenUIState>
        get() = _uiState




}