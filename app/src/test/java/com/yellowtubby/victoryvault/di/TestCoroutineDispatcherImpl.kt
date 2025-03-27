package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.core.di.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestCoroutineDispatcherImpl(val testCoroutineScheduler: TestCoroutineScheduler) :
    CoroutineDispatcherProvider {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher(testCoroutineScheduler)
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val ui: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher

}