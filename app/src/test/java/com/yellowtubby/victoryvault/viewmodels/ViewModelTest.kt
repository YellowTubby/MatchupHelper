package com.yellowtubby.victoryvault.viewmodels

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.yellowtubby.victoryvault.di.MatchupCoroutineDispatcher
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.di.TestCoroutineDispatcherImpl
import com.yellowtubby.victoryvault.di.matchUpModule
import com.yellowtubby.victoryvault.di.testModule
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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

    @After
    fun teardown() {
        mainViewModel?.onCleared()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun testEmitIntentAndCompareToState(intents: List<IntentClass>, expectedState: UIStateClass) {
        val scope: TestScope = koinTestRule.koin.get<ScopeProvider>().scope as TestScope
        val dispatcher: MatchupCoroutineDispatcher = koinTestRule.koin.get<MatchupCoroutineDispatcher>()
        // Ensure we use UnconfinedTestDispatcher
        // Start the test block with the correct dispatcher context
        runTest(dispatcher.ui) {
            println("Test started: Ensuring TestScope is active: ${scope.isActive}")
            // Launch emission of intents in the scope
            scope.launch(dispatcher.ui) {
                println("Emitting intents...")

                intents.forEach {
                    println("Emitting intent: $it")
                    mainViewModel?.emitIntent(it)
                }
            }
            advanceUntilIdle()
            // Allow the emission to process, UnconfinedTestDispatcher should run immediately

            // Collect emitted state values
            println("Collecting UI state...")
            mainViewModel?.uiState?.test {
                // Collect the most recent emitted state
                val state = awaitItem()
                println("Collected state: $state")
                // Assert that the collected state matches the expected state
                assertEquals(expectedState, state)
            }
        }

    }



}
