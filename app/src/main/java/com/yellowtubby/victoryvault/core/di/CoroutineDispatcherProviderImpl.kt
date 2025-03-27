package com.yellowtubby.victoryvault.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val ui: CoroutineDispatcher
        get() = Dispatchers.Main
    override val default : CoroutineDispatcher
        get() = Dispatchers.Default
}