package com.yellowtubby.victoryvault.general

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.repositories.ChampionInfoRepository
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel constructor(
    private val sharedFlowProvider: SharedFlowProvider,
    protected val coroutineDispatcher: MatchupCoroutineDispatcher
): ViewModel() {

    protected val matchupRepository: MatchupRepository by inject(MatchupRepository::class.java)
    protected val championInfoRepository: ChampionInfoRepository by inject(ChampionInfoRepository::class.java)
    protected val _intentFlow = sharedFlowProvider.getMutableSharedFlow()
    val intentFlow = sharedFlowProvider.getSharedFlow()

    abstract suspend fun handleIntent(intent: ApplicationIntent)
    abstract val filterFunction : (ApplicationIntent) -> Boolean

    fun emitIntent(intent : ApplicationIntent){
        viewModelScope.launch {
            _intentFlow.emit(intent)
        }
    }

    protected fun collectSharedFlow() {
        viewModelScope.launch {
            intentFlow.filter { filterFunction.invoke(it) }.collect { intent ->
                handleIntent(intent)
            }
        }
    }
}