package com.yellowtubby.victoryvault.core.di

import com.yellowtubby.victoryvault.core.presentation.ApplicationIntent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface SharedFlowProvider {
    fun getSharedFlow() : SharedFlow<ApplicationIntent>
    fun getMutableSharedFlow(): MutableSharedFlow<ApplicationIntent>
}
