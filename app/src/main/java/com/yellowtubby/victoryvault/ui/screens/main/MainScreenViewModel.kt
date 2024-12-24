package com.yellowtubby.victoryvault.ui.screens.main

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.model.FilterType
import com.yellowtubby.victoryvault.model.MatchupFilter
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.ui.screens.addmatchup.AddMatchupIntent
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.ui.screens.uicomponents.SnackbarMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class MainScreenViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher
) : BaseViewModel<MainScreenUIState>(sharedFlowProvider,coroutineDispatcher) {

    override val _uiState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(
        MAIN_SCREEN_INIT_STATE
    )
    protected val getAllChampionsUseCase: GetAllChampionsUseCase by inject(GetAllChampionsUseCase::class.java)


    init {
        viewModelScope.launch {
            launch {
                collectSharedFlow()
            }
            launch {
                collectAllChampionUseCase()
            }
        }
    }

    private suspend fun collectAllChampionUseCase() {
        getAllChampionsUseCase().collect {
            val allChampions = it
            val allDefinedChampions = matchupRepository.getAllChampions()
            val allMatchups = matchupRepository.getAllMatchups()
            val currentChampion = allDefinedChampions.firstOrNull()

            var role: Role? = Role.TOP;
            val filterRole: MutableList<MatchupFilter> = mutableListOf()

            currentChampion?.let {
                if (allMatchups.isNotEmpty()) {
                    val roleGrouping = allMatchups.groupingBy { it.role }
                    role = roleGrouping.eachCount().maxOf { it.key }
                    filterRole.add(MatchupFilter(FilterType.ROLE) {
                        it.role == role
                    })
                }
            }

            val currentFilters = _uiState.value.filterList + filterRole
            withContext(coroutineDispatcher.ui) {
                _intentFlow.tryEmit(
                    AddMatchupIntent.LocalDataChanged(
                        allChampions,
                        allChampions[0],
                        currentChampion,
                        role
                    )
                )
                _uiState.value = _uiState.value.copy(
                    definedChampion = allDefinedChampions,
                    currentChampion = currentChampion,
                    currentRole = role,
                    filterList = currentFilters,
                    matchupsForCurrentChampion = allMatchups.filter {
                        it.orig == currentChampion
                    },
                    loading = false
                )
            }
        }
    }

    override suspend fun handleIntent(intent: ApplicationIntent) {
            when (intent) {
                is MainScreenIntent.MultiSelectMatchups -> {
                    val selectedChampions =
                        if (_uiState.value.selectedMatchups.contains(intent.matchup)) {
                            _uiState.value.selectedMatchups - intent.matchup
                        } else {
                            _uiState.value.selectedMatchups + intent.matchup
                        }
                    _uiState.value = _uiState.value.copy(
                        selectedMatchups = selectedChampions
                    )
                    _intentFlow.tryEmit(
                        MainActivityIntent.UpdatedSelectedChampions(
                            selectedChampions.size
                        )
                    )
                }

                is MainScreenIntent.StartMultiSelectChampion -> {
                    _intentFlow.tryEmit(
                        MainActivityIntent.MultiSelectChanged(intent.enabled)
                    )
                    _uiState.value =
                        _uiState.value.copy(
                            multiSelectEnabled = intent.enabled,
                            selectedMatchups = if (!intent.enabled) emptyList() else _uiState.value.selectedMatchups
                        )
                }

                is MainScreenIntent.FilterListChanged -> TODO()

                is MainScreenIntent.SelectedMatchup -> {
                    _uiState.value = _uiState.value.copy(
                        loading = true
                    )
                    _uiState.value = _uiState.value.copy(
                        currentChampion = intent.matchup.enemy,
                        loading = false
                    )
                }

                is MainScreenIntent.TextFilterChanged -> {
                    _uiState.value = _uiState.value.copy(
                        textQuery = intent.filter
                    )
                }

                is MainScreenIntent.DeleteSelected -> {
                    _uiState.value = _uiState.value.copy(loading = true)
                    val currentChampion = _uiState.value.currentChampion
                    val currentRole = _uiState.value.currentRole
                    withContext(coroutineDispatcher.io) {
                        matchupRepository.deleteMatchups(
                            currentChampion!!.name,
                            currentRole!!,
                            intent.selectedMatchupsToDelete
                        )
                        val newMatchupList = matchupRepository.getAllMatchups()
                        withContext(coroutineDispatcher.ui) {
                            _uiState.value = _uiState.value.copy(
                                matchupsForCurrentChampion = newMatchupList,
                                selectedMatchups = emptyList(),
                                multiSelectEnabled = false
                            )

                            _intentFlow.tryEmit(
                                MainActivityIntent.MultiSelectChanged(isEnabled = false)
                            )
                            _uiState.value = _uiState.value.copy(loading = true)
                        }
                    }

                }

                is MainScreenIntent.RoleChanged -> {
                    _uiState.value = _uiState.value.copy(
                        loading = true
                    )
                    val currentListWithoutRoleFilter =
                        _uiState.value.filterList.filter {
                            it.type != FilterType.ROLE
                        }
                    val filterList = mutableStateOf(
                        currentListWithoutRoleFilter +
                                MatchupFilter(FilterType.ROLE) { matchup ->
                                    intent.role == matchup.role
                                }
                    )

                    _intentFlow.tryEmit(AddMatchupIntent.RoleChanged(intent.role))

                    _uiState.value = _uiState.value.copy(
                        filterList = filterList.value,
                        currentRole = intent.role,
                        loading = false
                    )
                }

                is MainScreenIntent.SelectChampion -> {
                    _uiState.value = _uiState.value.copy(
                        loading = true
                    )
                    val currentChampion = intent.champion
                    withContext(coroutineDispatcher.io) {
                        val matchupsForNewChamp =
                            matchupRepository.getAllMatchups().filter { matchup ->
                                matchup.orig == intent.champion
                            }
                        withContext(coroutineDispatcher.ui) {
                            _intentFlow.emit(AddMatchupIntent.CurrentChampionChanged(currentChampion))
                            _uiState.value = _uiState.value.copy(
                                currentChampion = currentChampion,
                                matchupsForCurrentChampion = matchupsForNewChamp,
                                loading = false
                            )
                        }
                    }
                }

                is MainScreenIntent.AddChampion -> {
                    _uiState.value = _uiState.value.copy(
                        snackBarMessage = Pair(false, SnackbarMessage()),
                        loading = true
                    )
                    _intentFlow.tryEmit(
                        MainActivityIntent.FabExpandedStateChanged(isExpanded = false)
                    )
                    if (!uiState.value.definedChampion.contains(intent.champion)) {
                        withContext(coroutineDispatcher.io) {
                            matchupRepository.addChampion(intent.champion)
                            val definedChampions = matchupRepository.getAllChampions()
                            withContext(coroutineDispatcher.ui) {
                                _uiState.value = _uiState.value.copy(
                                    currentChampion = intent.champion,
                                    definedChampion = definedChampions,
                                    snackBarMessage = Pair(
                                        true, SnackbarMessage(
                                            title = "Info",
                                            description = "${intent.champion.name} has been added",
                                            type = SnackBarType.SUCCESS
                                        )
                                    ),
                                    loading = false
                                )
                            }
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            snackBarMessage = Pair(
                                true, SnackbarMessage(
                                    title = "Error",
                                    description = "Champion is already defined",
                                    SnackBarType.ERROR
                                )
                            )
                        )
                    }
                }
            }
        }

        override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is MainScreenIntent }


}