package com.yellowtubby.victoryvault.feature.matchupdetails.presentation.screen

import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.matchups.UpdateMatchUpUseCase
import com.yellowtubby.victoryvault.domain.userdata.UserDataUseCase
import com.yellowtubby.victoryvault.core.presentation.BaseViewModel
import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class MatchupViewModel: BaseViewModel<MatchupScreenUIState>() {

    private val updateMatchUpUseCase: UpdateMatchUpUseCase by inject(UpdateMatchUpUseCase::class.java)
    private val updateCurrentMatchupUseCase: UpdateCurrentMatchupUseCase by inject(UpdateCurrentMatchupUseCase::class.java)
    private val getCurrentUserData: UserDataUseCase by inject(UserDataUseCase::class.java)

    override val _uiState = MutableStateFlow(
        MATCHUP_SCREEN_INIT_STATE
    )



    init {
        Timber.d("INIT CALLED!")
        definedScope.launch {
            collectSharedFlow()
        }
        definedScope.launch {
            getCurrentUserData().collect {
                Timber.d(
                    "$it"
                )
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
                    updateMatchUpUseCase(prevMatch)
                    updateCurrentMatchupUseCase(prevMatch)
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