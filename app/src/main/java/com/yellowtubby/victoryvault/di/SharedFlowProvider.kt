package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.ui.ApplicationIntent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface SharedFlowProvider {
    fun getSharedFlow() : SharedFlow<ApplicationIntent>
    fun getMutableSharedFlow(): MutableSharedFlow<ApplicationIntent>
}
