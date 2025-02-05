package com.yellowtubby.victoryvault.viewmodels

import android.util.Log
import com.yellowtubby.victoryvault.di.ScopeProvider
import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.BaseDefinedChampionUseCase
import com.yellowtubby.victoryvault.domain.champions.ChampionListUseCase
import com.yellowtubby.victoryvault.domain.champions.GetDefinedChampionsUseCase
import com.yellowtubby.victoryvault.model.Champion
import com.yellowtubby.victoryvault.model.Role
import com.yellowtubby.victoryvault.model.UserData
import com.yellowtubby.victoryvault.repositories.MatchupRepository
import com.yellowtubby.victoryvault.ui.MainActivityViewModel
import com.yellowtubby.victoryvault.ui.screens.main.MAIN_SCREEN_INIT_STATE
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenIntent
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenUIState
import com.yellowtubby.victoryvault.ui.screens.main.MainScreenViewModel
import com.yellowtubby.victoryvault.ui.uicomponents.SnackBarType
import com.yellowtubby.victoryvault.ui.uicomponents.SnackbarMessage
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declareMock

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
                snackBarMessage= Pair(
                    true,
                    SnackbarMessage(title="Info", description="Ahri has been added", type=SnackBarType.SUCCESS))
            )
        )

        // Assert: Verify addChampionUseCase was called
        coVerify(exactly = 1) { useCaseMock.invoke(testedChampion) }
        // Assert: Verify getChampionsUseCase was called
        coVerify(exactly = 1) { getCaseMock.invoke() }

    }






}