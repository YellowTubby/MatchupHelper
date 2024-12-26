package com.yellowtubby.victoryvault.ui.screens.matchup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.UpdateMatchUpUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class MatchupViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
) : BaseViewModel<MatchupScreenUIState>(sharedFlowProvider, coroutineDispatcher) {

    protected val updateMatchUpUseCase: UpdateMatchUpUseCase by inject(UpdateMatchUpUseCase::class.java)
    protected val getCurrentUserData: GetCurrentUserDataUseCase by inject(GetCurrentUserDataUseCase::class.java)

    override val _uiState = MutableStateFlow(
        MATCHUP_SCREEN_INIT_STATE
    )

    init {
        viewModelScope.launch {
            collectSharedFlow()
        }
        viewModelScope.launch {
            getCurrentUserData().collect {
                Log.d("SERJ", "MATCH SCREEN DATA: match - ${it.currentMatchup.orig.name} - ${it.currentMatchup.enemy.name}, role - ${it.currentRole}, champion selected - ${it.selectedChampion?.name}")
                _uiState.value = _uiState.value.copy(
                    matchup = it.currentMatchup,
                    loading = false
                )
            }
        }
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
                            updateMatchUpUseCase(prevMatch)
                        }
                }
            }

            is MatchupScreenIntent.LoadMatchupInfo -> {
                _uiState.value = _uiState.value.copy(
                )
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



}