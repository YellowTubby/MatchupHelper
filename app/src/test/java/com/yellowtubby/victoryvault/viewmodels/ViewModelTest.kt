package com.yellowtubby.victoryvault.viewmodels

import app.cash.turbine.test
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.di.matchUpModule
import com.yellowtubby.victoryvault.di.testModule
import com.yellowtubby.victoryvault.ui.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule


open class ViewModelTest<ViewModelClass : BaseViewModel<UIStateClass>, UIStateClass : ApplicationUIState, IntentClass : ApplicationIntent> {
    open var mainViewModel: ViewModelClass? = null


    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            matchUpModule
        )
        loadKoinModules(
            testModule
        )
        allowOverride(true)
    }

    @get:Rule
    var chain: TestRule = MockProviderRule.create {
        mockkClass(it)
    }

    private var scope: TestScope? = null


    @Before
    fun start(){
        scope = koinTestRule.koin.get<ScopeProvider>().scope as TestScope
    }
    @After
    fun teardown() {
        scope?.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun testEmitIntentAndCompareToState(intents: List<IntentClass>, expectedState: UIStateClass) {
        val dispatcher: MatchupCoroutineDispatcher = koinTestRule.koin.get<MatchupCoroutineDispatcher>()
        runTest(dispatcher.ui) {
            scope?.launch(dispatcher.ui) {
                intents.forEach {
                    println("Emitting intent: $it")
                    mainViewModel?.emitIntent(it)
                }
            }
            runCurrent()
            advanceUntilIdle()
            mainViewModel?.uiState?.test {
                // Collect the most recent emitted state
                val state = expectMostRecentItem()
                println("Collected state: $state")
                // Assert that the collected state matches the expected state
                assertEquals(expectedState, state)
            }
        }

    }



}
