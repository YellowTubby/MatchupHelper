package com.yellowtubby.victoryvault.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.di.SharedFlowProvider
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

abstract class BaseViewModel<UIState : ApplicationUIState> : ViewModel() {

    protected val sharedFlowProvider: SharedFlowProvider by inject(SharedFlowProvider::class.java)
    protected val coroutineScope: ScopeProvider by inject(ScopeProvider::class.java)
    protected val coroutineDispatcher: MatchupCoroutineDispatcher by inject(MatchupCoroutineDispatcher::class.java)
    protected val definedScope = coroutineScope.scope ?: viewModelScope

    protected val _intentFlow = sharedFlowProvider.getMutableSharedFlow()
    val intentFlow = sharedFlowProvider.getSharedFlow()

    abstract suspend fun handleIntent(intent: ApplicationIntent)
    abstract val filterFunction: (ApplicationIntent) -> Boolean
    open val startFunction: () -> Unit = {}
    val uiState: StateFlow<UIState>
        get() = _uiState.asStateFlow()

    protected abstract val _uiState: MutableStateFlow<UIState>

    fun emitIntent(intent: ApplicationIntent) {
        definedScope.launch(coroutineDispatcher.ui) {
            Timber.d("emitIntent: emitting ${intent.javaClass.simpleName} viewModel: ${javaClass.simpleName}")
            println("emitIntent: emitting ${intent.javaClass.simpleName} viewModel: ${javaClass.simpleName}")
            _intentFlow.tryEmit(intent)
        }
    }


    protected suspend fun collectSharedFlow() {
        intentFlow
            .filter { filterFunction.invoke(it) }
            .onStart { startFunction() }
            .onEach {
                val prevValue = _uiState.value
                prevValue.snackBarMessage = Pair(false, SnackbarMessage())
                _uiState.value = prevValue
            }
            .collect { intent ->
                println("Received event: $${intent.javaClass.simpleName} viewModel: ${javaClass.simpleName}")
                Timber.d("Received event: $${intent.javaClass.simpleName} viewModel: ${javaClass.simpleName}")
                handleIntent(intent)
            }
    }

    public override fun onCleared() {
        Timber.d("onCleared: ${javaClass.simpleName}")
        super.onCleared()
    }

}