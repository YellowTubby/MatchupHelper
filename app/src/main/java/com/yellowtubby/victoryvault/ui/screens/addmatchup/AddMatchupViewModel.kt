package com.yellowtubby.victoryvault.ui.screens.addmatchup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.AddMatchUpUseCase
import com.yellowtubby.victoryvault.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetFilteredMatchupsUseCase
import com.yellowtubby.victoryvault.domain.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.createScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.inject

class AddMatchupViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
)
    : BaseViewModel<AddMatchupUiState>(sharedFlowProvider,coroutineDispatcher) {

    override val _uiState: MutableStateFlow<AddMatchupUiState> = MutableStateFlow(
        ADD_MATCHUP_INIT_STATE
    )
    private val addMatchUpUseCase : AddMatchUpUseCase by inject(AddMatchUpUseCase::class.java)
    private val getAllUserData : GetCurrentUserDataUseCase by inject(GetCurrentUserDataUseCase::class.java)
    private val getAllChampions : GetAllChampionsUseCase by inject(GetAllChampionsUseCase::class.java)


    init {
        viewModelScope.launch {
                collectSharedFlow()
            }

        viewModelScope.launch {
            combine(
                getAllUserData(),
                getAllChampions()
            ) {
                userData, champions ->  Pair(userData,champions)
            }.collect {
                _uiState.value = _uiState.value.copy(
                    currentRole = it.first.currentRole,
                    currentChampion = it.first.selectedChampion,
                    allChampions = it.second
                )
            }
        }
    }

    override suspend fun handleIntent(intent: ApplicationIntent) {
        when(intent) {
            is AddMatchupIntent.AddMatchup -> {
                _uiState.value = _uiState.value.copy(
                    loading = true
                )
                withContext(coroutineDispatcher.io) {
                    addMatchUpUseCase(intent.matchup)
                    withContext(coroutineDispatcher.ui) {
                        _uiState.value = _uiState.value.copy(
                            loading = false
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



}