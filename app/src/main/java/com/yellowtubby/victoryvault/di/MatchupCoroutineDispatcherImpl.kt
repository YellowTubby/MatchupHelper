package com.yellowtubby.victoryvault.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MatchupCoroutineDispatcherImpl : MatchupCoroutineDispatcher {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val ui: CoroutineDispatcher
        get() = Dispatchers.Main
    override val default : CoroutineDispatcher
        get() = Dispatchers.Default
}