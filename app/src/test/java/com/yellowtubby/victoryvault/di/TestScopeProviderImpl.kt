package com.yellowtubby.victoryvault.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope

class TestScopeProviderImpl(private val dispatcherImpl: MatchupCoroutineDispatcher) : ScopeProvider {
    override val scope: CoroutineScope
        get() = TestScope(dispatcherImpl.default)
}