package com.yellowtubby.victoryvault.viewmodels

import com.yellowtubby.victoryvault.domain.champions.AddDefinedChampionUseCase
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
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
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
        mainViewModel = MainScreenViewModel(get(),get(),get())
    }

    @Test
    fun mainScreenViewModelTest_MainStateFirst_ExpectedStateEmitted(){
        testEmitIntentAndCompareToState(emptyList(), MAIN_SCREEN_INIT_STATE)
    }

    @Test
    fun mainScreenViewModelTest_AddChampion_ExpectedStateEmitted(){

        val testedChampion = Champion("Ahri")

        val useCaseMock = declareMock<AddDefinedChampionUseCase>()
        val getCaseMock = declareMock<GetDefinedChampionsUseCase>()
        coEvery { useCaseMock.invoke(any()) }.coAnswers {
            println("CALLED2!")
            getCaseMock()
        }
        coEvery { getCaseMock.invoke() }.coAnswers {
            flow {
                emit(listOf(testedChampion))
            }
        }

        testEmitIntentAndCompareToState(
            listOf(MainScreenIntent.AddChampion(testedChampion)),
            MAIN_SCREEN_INIT_STATE.copy(definedChampion = listOf(testedChampion))
        )
    }

}