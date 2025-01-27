package com.yellowtubby.victoryvault.viewmodels

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.yellowtubby.victoryvault.di.matchUpModule
import com.yellowtubby.victoryvault.di.testModule
import com.yellowtubby.victoryvault.general.BaseViewModel
import com.yellowtubby.victoryvault.ui.ApplicationIntent
import com.yellowtubby.victoryvault.ui.ApplicationUIState
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule


open class ViewModelTest<ViewModelClass : BaseViewModel<UIStateClass>, UIStateClass : ApplicationUIState , IntentClass : ApplicationIntent> {
    open var mainViewModel: ViewModelClass? = null


    @JvmField
    @Rule
    var chain: TestRule = RuleChain
        .outerRule(MockProviderRule.create {
            mockkClass(it)
        })
        .around(KoinTestRule.create {
            modules(
                matchUpModule,
                testModule
            )
            allowOverride(true)
        })

    fun testEmitIntentAndCompareToState(intents : List<IntentClass>, expectedState: UIStateClass) {
        runTest {
            intents.forEach {
                mainViewModel?.emitIntent(it)
            }
            turbineScope {
                mainViewModel?.uiState?.test {
                    val state = expectMostRecentItem()
                    System.out.println("${state}")
                    assertEquals(expectedState, state)
                }
            }
        }
    }

}
