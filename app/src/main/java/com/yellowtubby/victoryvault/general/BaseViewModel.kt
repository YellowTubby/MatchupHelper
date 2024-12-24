package com.yellowtubby.victoryvault.general

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel<UIState : ApplicationUIState> constructor(
    private val sharedFlowProvider: SharedFlowProvider,
    protected val coroutineDispatcher: MatchupCoroutineDispatcher
): ViewModel() {
    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)

    protected val _intentFlow = sharedFlowProvider.getMutableSharedFlow()
    val intentFlow = sharedFlowProvider.getSharedFlow()

    abstract suspend fun handleIntent(intent: ApplicationIntent)
    abstract val filterFunction : (ApplicationIntent) -> Boolean
    val uiState : StateFlow<UIState>
        get() = _uiState

    abstract protected val _uiState : MutableStateFlow<UIState>

    fun emitIntent(intent : ApplicationIntent){
        viewModelScope.launch {
            _intentFlow.emit(intent)
        }
    }

    protected suspend fun collectSharedFlow() {
        intentFlow.filter { filterFunction.invoke(it) }.collect { intent ->
            handleIntent(intent)
        }
    }


}