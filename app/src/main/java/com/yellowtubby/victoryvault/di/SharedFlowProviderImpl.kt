package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.ui.ApplicationIntent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SharedFlowProviderImpl : SharedFlowProvider {
    private val sharedFlow : MutableSharedFlow<ApplicationIntent> = MutableSharedFlow(replay = 1,
        extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_LATEST)

    override fun getSharedFlow(): SharedFlow<ApplicationIntent> {
        return sharedFlow.asSharedFlow()
    }

    override fun getMutableSharedFlow() : MutableSharedFlow<ApplicationIntent> {
        return sharedFlow
    }

}