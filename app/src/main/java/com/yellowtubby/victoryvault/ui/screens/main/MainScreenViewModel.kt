package com.yellowtubby.victoryvault.ui.screens.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.domain.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetCurrentUserDataUseCase
import com.yellowtubby.victoryvault.domain.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetFilteredMatchupsUseCase
import com.yellowtubby.victoryvault.domain.RemoveMatchUpsUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentMatchupUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentRoleUseCase
import com.yellowtubby.victoryvault.domain.UpdateCurrentSelectedChampionUseCase
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.model.FilterType
import com.yellowtubby.victoryvault.model.Matchup
import com.yellowtubby.victoryvault.model.MatchupFilter
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.ui.screens.addmatchup.AddMatchupIntent
import com.yellowtubby.victoryvault.ui.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class MainScreenViewModel(
    sharedFlowProvider: SharedFlowProvider,
    coroutineDispatcher: MatchupCoroutineDispatcher,
    getCurrentUserDataUseCase: GetCurrentUserDataUseCase
) : BaseViewModel<MainScreenUIState>(sharedFlowProvider,coroutineDispatcher) {

    override val _uiState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(
        MAIN_SCREEN_INIT_STATE
    )
    private val getAllChampionsUseCase: GetAllChampionsUseCase by inject(GetAllChampionsUseCase::class.java)
    private val getDefinedChampionsUseCase: GetDefinedChampionsUseCase by inject(GetDefinedChampionsUseCase::class.java)
    private val addChampionUseCase: BaseDefinedChampionUseCase by inject(BaseDefinedChampionUseCase::class.java, qualifier = named("add"))
    private val deleteMatchupsUseCase: RemoveMatchUpsUseCase by inject(RemoveMatchUpsUseCase::class.java)
    private val getAllMatchupsUseCase: GetFilteredMatchupsUseCase by inject(GetFilteredMatchupsUseCase::class.java)
    private val updateCurrentRole: UpdateCurrentRoleUseCase by inject(UpdateCurrentRoleUseCase::class.java)
    private val updateCurrentMatchup: UpdateCurrentMatchupUseCase by inject(UpdateCurrentMatchupUseCase::class.java)
    private val updateCurrentSelectedChampion: UpdateCurrentSelectedChampionUseCase by inject(UpdateCurrentSelectedChampionUseCase::class.java)

    data class ApplicationDataState(
        val allChampions: List<Champion>,
        val definedChampions: List<Champion>,
        val allMatchups: List<Matchup>,
        val userData: UserData
    )

    init {
        viewModelScope.launch {
            launch {
                collectSharedFlow()
            }
            launch {
                combine(
                    getAllChampionsUseCase(),
                    getDefinedChampionsUseCase(),
                    getAllMatchupsUseCase(),
                    getCurrentUserDataUseCase()
                ) {
                    allChampions, allDefinedChampions, allMatchups, userData ->
                    ApplicationDataState(allChampions,allDefinedChampions,allMatchups,userData)
                }.collect {
                    Log.d("SERJ", "MAIN SCREEN DATA: ${it.allMatchups.size} ")
                    handleCollectedData(it)
                }
            }
        }
    }

    private suspend fun handleCollectedData(
        applicationDataState: ApplicationDataState
    ) {
        val allChampions = applicationDataState.allChampions
        val currentChampion = if(applicationDataState.userData.selectedChampion.name == "NAN") applicationDataState.definedChampions.first() else applicationDataState.userData.selectedChampion
        var role: Role = Role.NAN
        val filterRole: MutableList<MatchupFilter> = mutableListOf()

        if (applicationDataState.allMatchups.isNotEmpty()) {
            val roleGrouping = applicationDataState.allMatchups.groupingBy { it.role }
            role = if(applicationDataState.userData.currentRole == Role.NAN) roleGrouping.eachCount().maxOf { it.key } else applicationDataState.userData.currentRole
            filterRole.add(MatchupFilter(FilterType.ROLE) {
                it.role == role
            })
        }

        val currentFilters = _uiState.value.filterList + filterRole
        var allMatchups : MutableList<Matchup> = mutableListOf()
        allMatchups.addAll(applicationDataState.allMatchups.filter { it.orig.name == currentChampion.name })

        currentFilters.forEach {
            filter ->
            allMatchups = allMatchups.filter{
                filter.filterFunction(it)
            }.toMutableList()
        }

        if(applicationDataState.userData.selectedChampion.name == "NAN"){
            updateCurrentSelectedChampion(currentChampion)
        }
        if(applicationDataState.userData.currentRole == Role.NAN){
            updateCurrentRole(role)
        }
        withContext(coroutineDispatcher.ui) {
            _uiState.value = _uiState.value.copy(
                definedChampion = applicationDataState.definedChampions,
                currentChampion = currentChampion,
                currentRole = role,
                matchupsForCurrentChampion = allMatchups,
                loading = allChampions.isEmpty()
            )
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
                    if(_uiState.value.currentRole != intent.role){
                        _uiState.value = _uiState.value.copy(
                            loading = true
                        )
                        updateCurrentRole(intent.role)
                    }
                }

                is MainScreenIntent.SelectChampion -> {
                    if(_uiState.value.currentChampion != intent.champion){
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