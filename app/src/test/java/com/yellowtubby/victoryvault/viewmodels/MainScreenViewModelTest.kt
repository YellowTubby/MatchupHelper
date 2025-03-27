package com.yellowtubby.victoryvault.viewmodels

import com.yellowtubby.victoryvault.core.domain.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.feature.main.presentation.screens.MAIN_SCREEN_INIT_STATE
import com.yellowtubby.victoryvault.feature.main.presentation.screens.MainScreenIntent
import com.yellowtubby.victoryvault.feature.main.presentation.screens.MainScreenUIState
import com.yellowtubby.victoryvault.feature.main.presentation.screens.MainScreenViewModel
import com.yellowtubby.victoryvault.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.uicomponents.SnackbarMessage
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get

class MainScreenViewModelTest : KoinTest, ViewModelTest<
        MainScreenViewModel,
        MainScreenUIState,
        MainScreenIntent
        >() {

    @Before
    fun setup() {
        super.start()
        mainViewModel = MainScreenViewModel()
    }

    @Test
    fun mainScreenViewModelTest_MainStateFirst_ExpectedStateEmitted(){
        testEmitIntentAndCompareToState(emptyList(), MAIN_SCREEN_INIT_STATE)
    }

    @Test
    fun mainScreenViewModelTest_StartMultiSelect_ExpectedStateEmitted(){
        testEmitIntentAndCompareToState(
            listOf(MainScreenIntent.StartMultiSelectChampion(true)),
            MAIN_SCREEN_INIT_STATE.copy(
                multiSelectEnabled = true
            ))
    }

    @Test
    fun mainScreenViewModelTest_StartMultiSelectAndSelectMatchup_SeeThatMatchupIsUpdated(){
        val testMatchup = Matchup()
        val updateMatchupMock = get<BaseDefinedChampionUseCase>(named("add"))
        testEmitIntentAndCompareToState(
            listOf(
                MainScreenIntent.StartMultiSelectChampion(true),
                MainScreenIntent.SelectedMatchup(testMatchup)
            ),
            MAIN_SCREEN_INIT_STATE.copy(
                multiSelectEnabled = true
            )
        )
    }


    @Test
    fun mainScreenViewModelTest_AddChampion_ExpectedStateEmitted(){
        val testedChampion = Champion("Ahri")
        val useCaseMock = get<BaseDefinedChampionUseCase>(named("add"))
        val getCaseMock = get<ChampionListUseCase>(named("defined"))
        coEvery { getCaseMock.invoke() } returns flow {
            emit(emptyList())
            delay(100)
            emit(listOf(testedChampion))
        }

        testEmitIntentAndCompareToState(
            listOf(MainScreenIntent.AddChampion(testedChampion)),
            MAIN_SCREEN_INIT_STATE.copy(
                loading = false,
                snackBarMessage = Pair(true,SnackbarMessage(stringRes=2131623965, type= SnackBarType.SUCCESS))
            )
        )

        // Assert: Verify addChampionUseCase was called
        coVerify(exactly = 1) { useCaseMock.invoke(testedChampion) }
        // Assert: Verify getChampionsUseCase was called
        coVerify(exactly = 1) { getCaseMock.invoke() }

    }






}