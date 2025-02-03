package com.yellowtubby.victoryvault.di

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.turbineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope

class TestScopeProviderImpl(
    private val testCoroutineScheduler: TestCoroutineScheduler,
    private val matchupCoroutineDispatcher: MatchupCoroutineDispatcher
) : ScopeProvider {

    override val scope = TestScope(matchupCoroutineDispatcher.ui)


}