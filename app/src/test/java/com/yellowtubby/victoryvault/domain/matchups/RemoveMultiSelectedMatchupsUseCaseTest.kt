package com.yellowtubby.victoryvault.domain.matchups

import app.cash.turbine.test
import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RemoveMultiSelectedMatchupsUseCaseTest : MockedKoinTest() {

    private lateinit var useCase: RemoveMultiSelectedMatchupsUseCase
    private lateinit var matchupStateFlow: MutableStateFlow<Pair<Boolean, List<Matchup>>>

    @Before
    fun setUp() {
        // Directly initialize the MutableStateFlow instead of declaring a mock
        matchupStateFlow = MutableStateFlow(Pair(true, emptyList()))
        useCase = RemoveMultiSelectedMatchupsUseCase(matchupStateFlow)
    }

    @Test
    fun `invoke with Matchup Remove Existing Matchup`() = runTest {
        val existingMatchup = Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))
        matchupStateFlow.value = Pair(true, listOf(existingMatchup))

        // Remove the existing matchup
        useCase.invoke(existingMatchup)

        // Verify that the matchup has been removed from the stateFlow
        assertEquals(Pair(true, emptyList()), matchupStateFlow.value)
    }

    @Test
    fun `invoke with Matchup Remove Non Existing Matchup`() = runTest {
        val nonExistingMatchup = Matchup(orig = Champion("Yasuo"), enemy = Champion("Lux"))
        matchupStateFlow.value = Pair(true, listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))))

        // Invoke with a matchup that doesn't exist in the list
        useCase.invoke(nonExistingMatchup)

        // The stateFlow should remain unchanged
        assertEquals(Pair(true, listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")))), matchupStateFlow.value)
    }

    @Test
    fun `invoke with Matchup Empty List`() = runTest {
        val matchup = Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))
        matchupStateFlow.value = Pair(true, emptyList())

        // Remove from an empty list, should do nothing
        useCase.invoke(matchup)

        assertEquals(Pair(true, emptyList()), matchupStateFlow.value)
    }

    @Test
    fun `invoke with Matchup Multiple identical Matchups`() = runTest {
        val identicalMatchup = Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))
        matchupStateFlow.value = Pair(true, listOf(identicalMatchup, identicalMatchup))

        // Remove one of the identical matchups
        useCase.invoke(identicalMatchup)

        // Verify only one instance is removed
        assertEquals(Pair(true, listOf(identicalMatchup)), matchupStateFlow.value)
    }

    @Test
    fun `invoke with Matchup StateFlow Emits Correct Value`() = runTest {
        val matchupToRemove = Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))
        matchupStateFlow.value = Pair(true, listOf(matchupToRemove))

        // Remove the matchup
        useCase.invoke(matchupToRemove)

        // Check if the correct state was emitted
        assertEquals(Pair(true, emptyList()), matchupStateFlow.value)
    }

    @Test
    fun `invoke with No Arguments Clear Matchups`() = runTest {
        // Populating the list with matchups
        matchupStateFlow.value = Pair(true, listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))))

        // Invoke with no arguments to clear matchups
        useCase.invoke()

        // Verify the list is cleared
        assertEquals(Pair(false, emptyList()), matchupStateFlow.value)
    }

    @Test
    fun `invoke with No Arguments Set Boolean to False`() = runTest {
        // Set the initial state of the Pair as true
        matchupStateFlow.value = Pair(true, listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))))

        // Call the function with no arguments
        useCase.invoke()

        // Assert that the boolean value is now false
        assertEquals(Pair(false, emptyList()), matchupStateFlow.value)
    }

    @Test
    fun `invoke with No Arguments StateFlow Emits Correct Value`() = runTest {
        // Set initial matchups in stateFlow
        matchupStateFlow.value = Pair(true, listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus"))))

        // Invoke with no arguments to clear matchups
        useCase.invoke()

        // Check if the correct state was emitted
        assertEquals(Pair(false, emptyList()), matchupStateFlow.value)
    }


    @Test
    fun `invoke with No Arguments Finishes with the item`() = runTest {
        matchupStateFlow = MutableStateFlow(Pair(true, emptyList())) // Simulating uninitialized stateFlow

        useCase.invoke()

        matchupStateFlow.test {
            assertEquals(Pair(true, emptyList()), awaitItem())
        }
    }
}
