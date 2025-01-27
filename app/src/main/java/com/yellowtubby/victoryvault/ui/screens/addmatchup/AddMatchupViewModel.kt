package com.yellowtubby.victoryvault.ui.screens.addmatchup

import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.matchups.AddMatchUpUseCase
import com.yellowtubby.victoryvault.domain.champions.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.userdata.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.userdata.UserDataUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
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
    private val getAllUserData : UserDataUseCase by inject(UserDataUseCase::class.java)
    private val getAllChampions : ChampionListUseCase by inject(ChampionListUseCase::class.java, named("all"))


    init {
        definedScope.launch(coroutineDispatcher.ui) {
                collectSharedFlow()
            }

        definedScope.launch(coroutineDispatcher.ui) {
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