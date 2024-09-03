package com.yellowtubby.matchuphelper.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.matchuphelper.di.MatchupCoroutineDispatcher
import com.yellowtubby.matchuphelper.di.MatchupCoroutineDispatcherImpl
import com.yellowtubby.matchuphelper.repositories.ChampionInfoRepository
import com.yellowtubby.matchuphelper.repositories.MatchupRepository
import com.yellowtubby.matchuphelper.ui.screens.matchup.INIT_STATE
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupIntent
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupScreen
import com.yellowtubby.matchuphelper.ui.screens.matchup.MatchupUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
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

    val intentChannel: Channel<MatchupIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(coroutineDispatcher.ui) {
            intentChannel.consumeEach {
                when (it) {
                    is MatchupIntent.SelectChampion -> {
                        val selectedChampions =
                            if (_uiStateMatchupScreen.value.selectedChampions.contains(it.champion)) {
                                _uiStateMatchupScreen.value.selectedChampions - it.champion
                            } else {
                                _uiStateMatchupScreen.value.selectedChampions + it.champion
                            }

                        _uiStateMatchupScreen.value = _uiStateMatchupScreen.value.copy(
                            selectedChampions = selectedChampions
                        )
                        _uiStateAppBar.value = _uiStateAppBar.value.copy(
                            selectedAmount = selectedChampions.size
                        )
                    }

                    is MatchupIntent.StartMultiSelectChampion -> {
                        _uiStateAppBar.value = _uiStateAppBar.value.copy(
                            isInMultiSelect = it.enabled,
                            selectedAmount = if (!it.enabled) 0 else _uiStateAppBar.value.selectedAmount
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
                }
            }
        }
    }

    private val _uiStateMatchupScreen = MutableStateFlow<MatchupUiState>(
        INIT_STATE
    )
    val uiStateMatchupScreen: StateFlow<MatchupUiState> = _uiStateMatchupScreen
    private val _uiStateAppBar = MutableStateFlow<AppBarUIState>(
        APP_BAR_INIT_STATE
    )
    val uiStateAppBar: StateFlow<AppBarUIState> = _uiStateAppBar

    override fun onCleared() {
        super.onCleared()
        scope.close()
    }

}