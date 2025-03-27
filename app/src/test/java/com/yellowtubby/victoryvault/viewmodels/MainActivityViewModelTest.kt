package com.yellowtubby.victoryvault.viewmodels

import com.yellowtubby.victoryvault.app.presentation.screen.MAIN_ACTIVITY_STATE
import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityIntent
import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityUIState
import com.yellowtubby.victoryvault.app.presentation.screen.MainActivityViewModel
import org.junit.Test

import org.junit.Before
import org.koin.test.KoinTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityViewModelTest : KoinTest, ViewModelTest<
        MainActivityViewModel,
        MainActivityUIState,
        MainActivityIntent>() {

    @Before
    fun setup() {
         mainViewModel = MainActivityViewModel()
    }

    @Test
    fun mainActivityViewModel_SubscribedToUIFlow_INITStatShown() {
        testEmitIntentAndCompareToState(emptyList(), MAIN_ACTIVITY_STATE)
    }

    @Test
    fun mainActivityViewModel_StartMultiSelectIntentSent_CorrectLoadoutShown() {
        testEmitIntentAndCompareToState(
            listOf(MainActivityIntent.MultiSelectChanged(isEnabled = true)),
            MAIN_ACTIVITY_STATE.copy(multiSelectEnabled = true)
        )
    }

    @Test
    fun mainActivityViewModel_turnOffMultiSelect_CorrectLoadoutShown() {
        testEmitIntentAndCompareToState(
            listOf(
                MainActivityIntent.MultiSelectChanged(isEnabled = true),
                MainActivityIntent.MultiSelectChanged(isEnabled = false)
            ),MAIN_ACTIVITY_STATE.copy(multiSelectEnabled = false)
        )
    }


    @Test
    fun mainActivityViewModel_EmitFab_CorrectLoadoutShown() {
        testEmitIntentAndCompareToState(
            listOf(MainActivityIntent.FabExpandedStateChanged(isExpanded = true)),
            MAIN_ACTIVITY_STATE.copy(isFabExpanded = true)
        )
    }

    @Test
    fun mainActivityViewModel_turnOffFab_CorrectLoadoutShown() {
        testEmitIntentAndCompareToState(
            listOf(
                MainActivityIntent.FabExpandedStateChanged(isExpanded = true),
                MainActivityIntent.FabExpandedStateChanged(isExpanded = false)
            ),
            MAIN_ACTIVITY_STATE.copy(isFabExpanded = false)
        )
    }



    @Test
    fun mainActivityViewModel_ChangedPage_CorrectLoadoutShown() {
        testEmitIntentAndCompareToState(
            listOf(
                MainActivityIntent.NavigatedBottomBar( 2),
                MainActivityIntent.NavigatedBottomBar( 1),
                MainActivityIntent.NavigatedBottomBar( 3)
            ),
            MAIN_ACTIVITY_STATE.copy(selectedBottomBarIndex = 3)
        )
    }

}