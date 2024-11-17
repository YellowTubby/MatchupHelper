package com.yellowtubby.victoryvault.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.ui.model.Champion
import com.yellowtubby.victoryvault.ui.model.FilterType
import com.yellowtubby.victoryvault.ui.model.Matchup
import com.yellowtubby.victoryvault.ui.model.MatchupFilter
import com.yellowtubby.victoryvault.ui.model.Role
import com.yellowtubby.victoryvault.ui.screens.add.ADD_CHAMP_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.add.ADD_MATCHUP_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.add.AddChampionIntent
import com.yellowtubby.victoryvault.ui.screens.add.AddChampionUiState
import com.yellowtubby.victoryvault.ui.screens.add.AddMatchupIntent
import com.yellowtubby.victoryvault.ui.screens.add.AddMatchupUiState
import com.yellowtubby.victoryvault.ui.screens.champion.MATCHUP_SCREEN_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.champion.MatchupScreenIntent
import com.yellowtubby.victoryvault.ui.screens.champion.MatchupScreenUiState
import com.yellowtubby.victoryvault.ui.screens.matchup.MAIN_SCREEN_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.matchup.MainScreenUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.inject

class MatchupViewModel : ViewModel(), KoinScopeComponent {


    override val scope: Scope = createScope(this)

    private val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    private val championInfoRepository: ChampionInfoRepository by inject(ChampionInfoRepository::class.java)
    private val coroutineDispatcher: MatchupCoroutineDispatcher by scope.inject<MatchupCoroutineDispatcher>()

    private lateinit var allMatchups : List<Matchup>
    private lateinit var allDefinedChampions : List<Champion>
    private lateinit var allChampions : List<Champion>

    val intentChannel: Channel<ApplicationIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(coroutineDispatcher.ui) {
            intentChannel.consumeEach {
                when (it) {
                    is MainScreenIntent -> {
                        handleMainScreenIntent(it)
                    }

                    is AddChampionIntent -> {
                        handleAddChampionScreenIntent(it)
                    }

                    is MatchupScreenIntent -> {
                        handleMatchupScreenIntent(it)
                    }

                    is AddMatchupIntent -> {
                        handleAddMatchupIntent(it)
                    }
                }
            }
        }
    }

    private suspend fun handleAddMatchupIntent(it: AddMatchupIntent) {
        when(it){
            is AddMatchupIntent.SelectedChampion -> {
                _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                    selectedChampion = it.champion
                )
            }

            is AddMatchupIntent.AddMatchup -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                withContext(coroutineDispatcher.io) {
                    val addMatch = async { matchupRepository.addMatchup(it.matchup) }
                    addMatch.await()
                    val matchups = mutableStateOf(matchupRepository.getAllMatchups())
                    withContext(coroutineDispatcher.ui) {
                        _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                            matchupsForCurrentChampion = matchups.value
                        )
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun handleMatchupScreenIntent(it: MatchupScreenIntent) {
        when(it){
            is MatchupScreenIntent.WinLossChanged -> {
                withContext(coroutineDispatcher.ui){
                    var prevMatch = _uiStateMatchupScreen.value.matchup
                    prevMatch = prevMatch.copy(
                        numTotal = prevMatch.numTotal + 1
                    )
                    if(it.isWon){
                        prevMatch = prevMatch.copy(
                            numWins = prevMatch.numWins + 1
                        )
                    }
                    Log.d("SERJ", "handleMatchupScreenIntent: matchupToInsert ${prevMatch.numTotal} + ${prevMatch.numWins}")
                    withContext(coroutineDispatcher.io){
                        matchupRepository.updateMatchup(prevMatch)
                    }
                    val mutableList : MutableList<Matchup> = allMatchups.toMutableList()
                    mutableList.removeIf {
                        it.orig == prevMatch.orig && it.enemy == prevMatch.enemy
                    }
                    mutableList.add(prevMatch)
                    allMatchups = mutableList
                    _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                        matchup = allMatchups.first {
                            prevMatch.orig == it.orig && prevMatch.enemy == it.enemy
                        }
                    )
                }
            }
        }

    }

    private suspend fun handleAddChampionScreenIntent(it: AddChampionIntent) {
        when(it){
            is AddChampionIntent.ChampionSelected -> {
                _uiStateAddChampionScreen.value = _uiStateAddChampionScreen.value.copy(
                    championSelected = it.champion,
                )
            }

            is AddChampionIntent.AddChampion -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                withContext(coroutineDispatcher.io) {
                    matchupRepository.addChampion(it.champion)
                    val definedChampions = matchupRepository.getAllChampions()
                    withContext(coroutineDispatcher.ui) {
                        _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                            currentChampion = it.champion,
                            definedChampion = definedChampions
                        )
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = false
                        )
                    }
                }
            }
        }

    }

    private suspend fun handleMainScreenIntent(it: MainScreenIntent) {
        when(it){
            is MainScreenIntent.MultiSelectMatchups -> {
                val selectedChampions =
                    if (_uiStateMainScreen.value.selectedMatchups.contains(it.matchup)) {
                        _uiStateMainScreen.value.selectedMatchups - it.matchup
                    } else {
                        _uiStateMainScreen.value.selectedMatchups + it.matchup
                    }
                _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                    selectedMatchups = selectedChampions
                )
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    selectedAmount = selectedChampions.size
                )
            }

            is MainScreenIntent.StartMultiSelectChampion -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    isInMultiSelect = it.enabled,
                    selectedAmount = if (!it.enabled) 0 else _uiStateMainActivity.value.selectedAmount
                )
                _uiStateMainScreen.value =
                    _uiStateMainScreen.value.copy(
                        isInMultiSelect = it.enabled,
                        selectedMatchups = if (!it.enabled) emptyList() else _uiStateMainScreen.value.selectedMatchups
                    )
            }

            is MainScreenIntent.FilterListChanged -> TODO()

            is MainScreenIntent.SelectedMatchup -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                    currentChampion = it.matchup.enemy
                )
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = false
                )
            }

            is MainScreenIntent.TextFilterChanged -> {
                _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                    textQuery = it.filter
                )
            }

            is MainScreenIntent.DeleteSelected -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                val currentChampion = _uiStateMainScreen.value.currentChampion
                val currentRole = _uiStateMainScreen.value.currentRole
                withContext(coroutineDispatcher.io) {
                    matchupRepository.deleteMatchups(
                        currentChampion!!.name,
                        currentRole!!,
                        it.selectedMatchupsToDelete
                    )
                    val newMatchupList = matchupRepository.getAllMatchups()
                    withContext(coroutineDispatcher.ui) {
                        _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                            matchupsForCurrentChampion = newMatchupList,
                            selectedMatchups = emptyList(),
                            isInMultiSelect = false
                        )

                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            isInMultiSelect = false,
                            selectedAmount = 0,
                            loading = false
                        )

                    }
                }

            }

            is MainScreenIntent.RoleChanged -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                val currentListWithoutRoleFilter =
                    _uiStateMainScreen.value.filterList.filter {
                        it.type != FilterType.ROLE
                    }
                val filterList = mutableStateOf(
                    currentListWithoutRoleFilter +
                            MatchupFilter(FilterType.ROLE) { matchup ->
                                it.role == matchup.role
                            }
                )

                _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                    currentRole = it.role,
                )
                _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                    filterList = filterList.value,
                    currentRole = it.role,
                )
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = false
                )
            }

            MainScreenIntent.LoadLocalData -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                withContext(coroutineDispatcher.io) {
                    loadLocalData()

                    val firstDefined = allDefinedChampions.firstOrNull()
                    var role: Role? = Role.TOP;
                    val filterRole: MutableList<MatchupFilter> = mutableListOf()

                    firstDefined?.let {
                        allMatchups = mutableStateOf( matchupRepository.getAllMatchups()).value
                        Log.d("SERJ", "handleMainScreenIntent: $allMatchups")
                        if(allMatchups.isNotEmpty()){
                            val roleGrouping = allMatchups.groupingBy { it.role }
                            role = roleGrouping.eachCount().maxOf { it.key }
                            filterRole.add(MatchupFilter(FilterType.ROLE) {
                                it.role == role
                            })
                        }
                    }

                    val currentFilters = _uiStateMainScreen.value.filterList + filterRole
                    withContext(coroutineDispatcher.ui) {
                        _uiStateAddMatchupScreen.value =
                            _uiStateAddMatchupScreen.value.copy(
                                allChampions = allChampions,
                                selectedChampion = allChampions[0],
                                currentChampion = firstDefined,
                                currentRole = role
                            )
                        _uiStateAddChampionScreen.value =
                            _uiStateAddChampionScreen.value.copy(
                                allChampions = allChampions,
                            )
                        _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                            definedChampion = allDefinedChampions,
                            currentChampion = firstDefined,
                            currentRole = role,
                            filterList = currentFilters,
                            matchupsForCurrentChampion = allMatchups.filter {
                                it.orig == firstDefined
                            }
                        )
                    }
                }

                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = false
                )
            }

            is MainScreenIntent.SelectChampion -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    loading = true
                )
                matchupRepository.selectChampion(championInfoRepository)
                withContext(coroutineDispatcher.io){
                    val matchupsForNewChamp = mutableStateOf(matchupRepository.getAllMatchups())
                    withContext(coroutineDispatcher.ui){
                        _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                            currentChampion = it.champion,
                        )
                        _uiStateMainScreen.value = _uiStateMainScreen.value.copy(
                            currentChampion = it.champion,
                            matchupsForCurrentChampion = matchupsForNewChamp.value
                        )
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = false
                        )
                    }
                }
            }

            is MainScreenIntent.NavigatedBottomBar -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    selectedBottomBarIndex = it.selectedIndex
                )
            }

            is MainScreenIntent.LoadMatchupInfo -> {
                val filterMatchup = it.matchup
                _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                    matchup = allMatchups.first {
                        it.enemy == filterMatchup.enemy && it.orig == filterMatchup.orig
                    }
                )

            }

            is MainScreenIntent.ShowBottomBar -> {
                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                    isBottomBarVisible = it.shouldShow
                )
            }
        }
    }

    private suspend fun loadLocalData() {
        allDefinedChampions = matchupRepository.getAllChampions()
        allChampions = championInfoRepository.getAllChampions()
        allMatchups = matchupRepository.getAllMatchups()
    }


    // Screen State Flows
    private val _uiStateMainActivity = MutableStateFlow<MainActivityUIState>(
        MAIN_ACTIVITY_STATE
    )
    val uiStateMainActivity: StateFlow<MainActivityUIState>
        get() = _uiStateMainActivity


    private val _uiStateMainScreen = MutableStateFlow<MainScreenUiState>(
        MAIN_SCREEN_INIT_STATE
    )
    val uiStateMainScreen: StateFlow<MainScreenUiState>
        get() = _uiStateMainScreen

    private val _uiStateAddChampionScreen = MutableStateFlow<AddChampionUiState>(
        ADD_CHAMP_INIT_STATE
    )
    val uiStateAddChampionScreen: StateFlow<AddChampionUiState>
        get() = _uiStateAddChampionScreen

    private val _uiStateAddMatchupScreen = MutableStateFlow<AddMatchupUiState>(
        ADD_MATCHUP_INIT_STATE
    )
    val uiStateAddMatchupScreen: StateFlow<AddMatchupUiState>
        get() = _uiStateAddMatchupScreen

    private val _uiStateMatchupScreen = MutableStateFlow<MatchupScreenUiState>(
        MATCHUP_SCREEN_INIT_STATE
    )
    val uiStateMatchupScreen: StateFlow<MatchupScreenUiState>
        get() = _uiStateMatchupScreen

    override fun onCleared() {
        scope.close()
        super.onCleared()
    }


}