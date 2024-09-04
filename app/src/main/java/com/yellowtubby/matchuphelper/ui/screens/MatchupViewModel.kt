package com.yellowtubby.matchuphelper.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.matchuphelper.di.MatchupCoroutineDispatcher
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepository
import com.yellowtubby.matchuphelper.repositories.MatchupRepository
import com.yellowtubby.matchuphelper.ui.screens.add.ADD_CHAMP_INIT_STATE
import com.yellowtubby.matchuphelper.ui.screens.add.ADD_MATCHUP_INIT_STATE
import com.yellowtubby.matchuphelper.ui.screens.add.AddChampionIntent
import com.yellowtubby.matchuphelper.ui.screens.add.AddChampionUiState
import com.yellowtubby.matchuphelper.ui.screens.add.AddMatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.add.AddMatchupUiState
import com.yellowtubby.matchuphelper.ui.screens.matchup.MATCH_SCREEN_INIT_STATE
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.inject

class MatchupViewModel : ViewModel(), KoinScopeComponent {


    override val scope: Scope
        get() = createScope(this)

    val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    val championInfoRepository: ChampionInfoRepository by inject(ChampionInfoRepository::class.java)
    val coroutineDispatcher: MatchupCoroutineDispatcher by scope.inject<MatchupCoroutineDispatcher>()

    val intentChannel: Channel<ApplicationIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(coroutineDispatcher.ui) {
            intentChannel.consumeEach {
                when (it) {
                    is MatchupIntent.MultiSelectChampion -> {
                        val selectedChampions =
                            if (_uiStateMatchupScreen.value.selectedChampions.contains(it.champion)) {
                                _uiStateMatchupScreen.value.selectedChampions - it.champion
                            } else {
                                _uiStateMatchupScreen.value.selectedChampions + it.champion
                            }
                        _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                            selectedChampions = selectedChampions
                        )
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            selectedAmount = selectedChampions.size
                        )
                    }

                    is MatchupIntent.StartMultiSelectChampion -> {
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            isInMultiSelect = it.enabled,
                            selectedAmount = if (!it.enabled) 0 else _uiStateMainActivity.value.selectedAmount
                        )
                        _uiStateMatchupScreen.value =
                            _uiStateMatchupScreen.value.copy(
                                isInMultiSelect = it.enabled,
                                selectedChampions = if (!it.enabled) emptyList() else _uiStateMatchupScreen.value.selectedChampions
                            )
                    }

                    MatchupIntent.DeleteSelected -> TODO()
                    is MatchupIntent.FilterListChanged -> TODO()
                    is MatchupIntent.TextFilterChanged -> {
                        _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                            textQuery = it.filter
                        )
                    }

                    is MatchupIntent.RoleChanged -> {
                        _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                            currentRole = it.role,
                        )
                        _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                            currentRole = it.role,
                        )
                    }

                    MatchupIntent.LoadLocalData -> {
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = true
                        )
                        withContext(coroutineDispatcher.io){
                            val definedChampions = matchupRepository.getAllChampions()
                            Log.d("SERJ", ": $definedChampions")
                            val allChampions = championInfoRepository.getAllChampions()
                            withContext(coroutineDispatcher.ui){
                                _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                                    allChampions = allChampions,
                                )
                                _uiStateAddChampionScreen.value = _uiStateAddChampionScreen.value.copy(
                                    allChampions = allChampions
                                )
                                _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                                    definedChampion = definedChampions,
                                    currentChampion = if(definedChampions.isNotEmpty()) definedChampions[0] else null
                                )
                            }
                        }

                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = false
                        )
                    }

                    is MatchupIntent.SelectChampion -> {
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = true
                        )
                        matchupRepository.selectChampion(championInfoRepository)
                        _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                            currentChampion = it.champion,
                        )
                        _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                            currentChampion = it.champion,
                        )
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = false
                        )
                    }

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
                            withContext(coroutineDispatcher.ui){
                                _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                                    currentChampion = it.champion,
                                    definedChampion = definedChampions
                                )
                                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                                    loading = false
                                )
                            }
                        }
                    }

                    is AddMatchupIntent.SelectedChampion -> {
                        _uiStateAddMatchupScreen.value = _uiStateAddMatchupScreen.value.copy(
                            selectedChampion = it.champion
                        )
                    }

                    is AddMatchupIntent.AddMatchup -> {
                        _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                            loading = true
                        )
                        withContext(coroutineDispatcher.io){
                            matchupRepository.addMatchup(it.matchup)
                            val matchups = matchupRepository.getAllMatchupsforChampion(it.matchup.orig)
                            withContext(coroutineDispatcher.ui){
                                _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                                    matchupsForCurrentChampion = matchups
                                )
                                _uiStateMainActivity.value = _uiStateMainActivity.value.copy(
                                    loading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    // Screen State Flows
    private val _uiStateMainActivity = MutableStateFlow<MainActivityUIState>(
        MAIN_ACTIVITY_STATE
    )
    val uiStateMainActivity: StateFlow<MainActivityUIState>
        get() = _uiStateMainActivity


    private val _uiStateMatchupScreen = MutableStateFlow<MatchupUiState>(
        MATCH_SCREEN_INIT_STATE
    )
    val uiStateMatchupScreen: StateFlow<MatchupUiState>
        get() = _uiStateMatchupScreen

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


    override fun onCleared() {
        super.onCleared()
        scope.close()
    }

}