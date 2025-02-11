package com.yellowtubby.victoryvault.ui.screens.main

import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.matchups.AddMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.GetMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.matchups.MatchupListUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.matchups.RemoveMultiSelectedMatchupsUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.userdata.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.domain.userdata.UserDataUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.model.FilterType
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.MatchupFilter
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.ui.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class MainScreenViewModel : BaseViewModel<MainScreenUIState>() {
    private val getAllChampionsUseCase: ChampionListUseCase by inject(
        ChampionListUseCase::class.java,
        qualifier = named("all")
    )
    private val getDefinedChampionsUseCase: ChampionListUseCase by inject(
        ChampionListUseCase::class.java,
        qualifier = named("defined")
    )
    private val addChampionUseCase: BaseDefinedChampionUseCase by inject(
        BaseDefinedChampionUseCase::class.java,
        qualifier = named("add")
    )
    private val deleteMatchupsUseCase: RemoveMatchUpsUseCase by inject(RemoveMatchUpsUseCase::class.java)
    private val getAllMatchupsUseCase: MatchupListUseCase by inject(MatchupListUseCase::class.java)
    private val getCurrentUserDataUseCase: UserDataUseCase by inject(UserDataUseCase::class.java)
    private val updateCurrentRole: UpdateCurrentRoleUseCase by inject(UpdateCurrentRoleUseCase::class.java)
    private val addMultiSelectedMatchupsUseCase: AddMultiSelectedMatchupsUseCase by inject(
        AddMultiSelectedMatchupsUseCase::class.java
    )
    private val removeMultiSelectedMatchupsUseCase: RemoveMultiSelectedMatchupsUseCase by inject(
        RemoveMultiSelectedMatchupsUseCase::class.java
    )
    private val getMultiSelectedMatchupsUseCase: GetMultiSelectedMatchupsUseCase by inject(
        GetMultiSelectedMatchupsUseCase::class.java
    )
    private val updateCurrentMatchup: UpdateCurrentMatchupUseCase by inject(
        UpdateCurrentMatchupUseCase::class.java
    )
    private val updateCurrentSelectedChampion: UpdateCurrentSelectedChampionUseCase by inject(
        UpdateCurrentSelectedChampionUseCase::class.java
    )

    override val _uiState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(
        MAIN_SCREEN_INIT_STATE
    )

    data class ApplicationDataState(
        val allChampions: List<Champion>,
        val definedChampions: List<Champion>,
        val allMatchups: List<Matchup>,
        val userData: UserData
    ) {
        override fun toString(): String {
            return "ApplicationDataState(allChampions=${allChampions.size}\n, definedChampions=${
                definedChampions.map {
                    it.name
                }
            }\n, allMatchups=\n${
                allMatchups.map {
                    "${it.orig.name} -> ${it.enemy.name}\n"
                }
            }\n, userData=$userData\n "
        }
    }

    init {

        definedScope.launch(coroutineDispatcher.ui) {
            println("Launching collectSharedFlow in ${javaClass.simpleName}")
            collectSharedFlow()
        }

        definedScope.launch(coroutineDispatcher.ui) {
            delay(200)
            getMultiSelectedMatchupsUseCase().collect {
                Timber.d("COLLECTED DATA: ${it.second}")
                _uiState.value = _uiState.value.copy(
                    selectedMatchups = it.second,
                    multiSelectEnabled = it.first
                )
            }
        }

        definedScope.launch(coroutineDispatcher.io) {
            delay(200)
            combine(
                getAllChampionsUseCase(),
                getDefinedChampionsUseCase(),
                getAllMatchupsUseCase(),
                getCurrentUserDataUseCase()
            ) { allChampions, allDefinedChampions, allMatchups, userData ->
                ApplicationDataState(allChampions, allDefinedChampions, allMatchups, userData)
            }.collect {
                handleCollectedData(it)
            }
        }
    }

    private suspend fun handleCollectedData(
        applicationDataState: ApplicationDataState
    ) {
        val allChampions = applicationDataState.allChampions
        val currentChampion = calculateCurrentChampion(applicationDataState)
        val role: Role = calculateAndUpdateCurrentRole(applicationDataState, currentChampion)
        val filterRole: MutableList<MatchupFilter> = mutableListOf()

        if (role != Role.NAN) {
            filterRole.add(MatchupFilter(FilterType.ROLE) {
                it.role == role
            })
        }

        val currentFilters = _uiState.value.filterList + filterRole
        var displayedMatchups: MutableList<Matchup> = mutableListOf()
        if (currentChampion != Champion.NAN) {
            displayedMatchups.addAll(applicationDataState.allMatchups.filter { it.orig.name == currentChampion.name })
        }

        currentFilters.forEach { filter ->
            displayedMatchups = displayedMatchups.filter {
                filter.filterFunction(it)
            }.toMutableList()
        }

        withContext(coroutineDispatcher.ui) {
            _uiState.value = _uiState.value.copy(
                definedChampion = applicationDataState.definedChampions,
                allChampions = applicationDataState.allChampions,
                currentChampion = currentChampion,
                currentRole = role,
                currentMatchupList = displayedMatchups,
                loading = allChampions.isEmpty()
            )
        }
    }

    private suspend fun calculateCurrentChampion(applicationDataState: MainScreenViewModel.ApplicationDataState): Champion {
        return if (applicationDataState.userData.selectedChampion.name == "NAN") {
            if (applicationDataState.definedChampions.isNotEmpty()) {
                val currentChampion = applicationDataState.definedChampions.first()
                updateCurrentSelectedChampion(currentChampion)
                currentChampion
            } else {
                Champion("NAN")
            }
        } else {
            applicationDataState.userData.selectedChampion
        }
    }

    override suspend fun handleIntent(intent: ApplicationIntent) {
        when (intent) {
            is MainScreenIntent.MultiSelectMatchups -> {
                val currentMatchups = _uiState.value.selectedMatchups
                if (currentMatchups.isEmpty()) {
                    Timber.d("ADDING ${intent.matchup}")
                    addMultiSelectedMatchupsUseCase(intent.matchup)
                } else if (currentMatchups.any {
                        it.orig.name == intent.matchup.orig.name &&
                                it.enemy.name == intent.matchup.enemy.name
                    }) {
                    Timber.d("REMOVING ${intent.matchup}")
                    removeMultiSelectedMatchupsUseCase(intent.matchup)
                } else {
                    Timber.d("ADDING ${intent.matchup}")

                    addMultiSelectedMatchupsUseCase(intent.matchup)
                }
            }

            is MainScreenIntent.StartMultiSelectChampion -> {
                _uiState.value =
                    _uiState.value.copy(
                        multiSelectEnabled = intent.enabled,
                        selectedMatchups = if (!intent.enabled) emptyList() else _uiState.value.selectedMatchups
                    )
                if (!intent.enabled) {
                    removeMultiSelectedMatchupsUseCase()
                } else {
                    addMultiSelectedMatchupsUseCase()
                }
            }

            is MainScreenIntent.FilterListChanged -> TODO()

            is MainScreenIntent.SelectedMatchup -> {
                updateCurrentMatchup(intent.matchup)
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
                    deleteMatchupsUseCase(
                        currentChampion,
                        currentRole,
                        intent.selectedMatchupsToDelete
                    )
                    withContext(coroutineDispatcher.ui) {
                        _uiState.value = _uiState.value.copy(
                            multiSelectEnabled = false
                        )
                        _intentFlow.tryEmit(
                            MainActivityIntent.MultiSelectChanged(isEnabled = false)
                        )
                        _uiState.value = _uiState.value.copy(loading = false)
                    }
                }

            }

            is MainScreenIntent.RoleChanged -> {
                if (_uiState.value.currentRole != intent.role) {
                    _uiState.value = _uiState.value.copy(
                        loading = true
                    )
                    updateCurrentRole(intent.role)
                }
            }

            is MainScreenIntent.SelectChampion -> {
                if (_uiState.value.currentChampion != intent.champion) {
                    _uiState.value = _uiState.value.copy(
                        loading = true
                    )
                    withContext(coroutineDispatcher.io) {
                        updateCurrentSelectedChampion(intent.champion)
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
                        addChampionUseCase(intent.champion)
                        withContext(coroutineDispatcher.ui) {
                            _uiState.value = _uiState.value.copy(
                                snackBarMessage = Pair(
                                    true, SnackbarMessage(
                                        title = "Info",
                                        description = "${intent.champion.name} has been added",
                                        type = SnackBarType.SUCCESS
                                    )
                                ),
                                loading = false,
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

    private suspend fun calculateAndUpdateCurrentRole(
        currentState: ApplicationDataState,
        currentChampion: Champion
    ): Role {
        if (currentChampion == Champion.NAN) {
            return Role.NAN
        }
        if (currentState.userData.currentRole != Role.NAN) {
            return currentState.userData.currentRole
        }
        var currentRole = Role.TOP
        val championMatchups =
            currentState.allMatchups.filter { it.orig.name == currentChampion.name }
        if (championMatchups.isNotEmpty()) {
            val roleGrouping = currentState.allMatchups.groupingBy { it.role }
            currentRole = roleGrouping.eachCount().maxOf { it.key }
            updateCurrentRole(role = currentRole)
        }

        return currentRole
    }

    override val filterFunction: (ApplicationIntent) -> Boolean
        get() = { it is MainScreenIntent }


}