package com.yellowtubby.victoryvault.di

import android.util.Log
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.MainActivityIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreen
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class SharedFlowProviderImpl : SharedFlowProvider {
    private val sharedFlow : MutableSharedFlow<ApplicationIntent> = MutableSharedFlow(replay = 1,
        extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_LATEST)

    override fun getSharedFlow(): Flow<ApplicationIntent> {
        return sharedFlow.asSharedFlow().onStart {
            emit(MainScreenIntent.LoadLocalData)
        }
    }

    override fun getMutableSharedFlow() : MutableSharedFlow<ApplicationIntent> {
        return sharedFlow
    }

}
