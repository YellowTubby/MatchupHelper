package com.yellowtubby.victoryvault.di

import com.yellowtubby.victoryvault.core.di.CoroutineDispatcherProvider
import com.yellowtubby.victoryvault.core.di.ScopeProvider
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope

class TestScopeProviderImpl(
    private val testCoroutineScheduler: TestCoroutineScheduler,
    private val matchupCoroutineDispatcher: CoroutineDispatcherProvider
) : ScopeProvider {

    override val scope = TestScope(matchupCoroutineDispatcher.ui)


}