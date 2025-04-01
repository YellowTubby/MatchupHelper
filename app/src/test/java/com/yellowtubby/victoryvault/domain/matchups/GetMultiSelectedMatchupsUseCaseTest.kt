package com.yellowtubby.victoryvault.domain.matchups

import app.cash.turbine.test
import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetMultiSelectedMatchupsUseCaseTest : MockedKoinTest() {

    private lateinit var stateFlow: MutableStateFlow<Pair<Boolean, List<Matchup>>>
    private lateinit var useCase: GetMultiSelectedMatchupsUseCase

    @Before
    fun setUp() {
        stateFlow = MutableStateFlow(false to emptyList())
        useCase = GetMultiSelectedMatchupsUseCase(stateFlow)
    }

    @Test
    fun `Invoke returns the stateFlow as a flow`() = runTest {
        assertEquals(stateFlow.first(), useCase.invoke().first())
    }

    @Test
    fun `Emission of current value`() = runTest {
        useCase.invoke().test {
            assertEquals(false to emptyList<Matchup>(), awaitItem())
        }
    }

    @Test
    fun `Emission of new values`() = runTest {
        useCase.invoke().test {
            awaitItem()
            stateFlow.value = true to listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
            assertEquals(true to listOf(Matchup(Champion("Aatrox"), Champion("Nasus"))), awaitItem())
        }
    }

    @Test
    fun `Check for Boolean value emission`() = runTest {
        useCase.invoke().test {
            assertTrue(awaitItem().first is Boolean)
        }
    }

    @Test
    fun `Check for Matchup List emission`() = runTest {
        useCase.invoke().test {
            assertTrue(awaitItem().second is List<Matchup>)
        }
    }

    @Test
    fun `Empty Matchup list`() = runTest {
        stateFlow.value = false to emptyList()
        assertEquals(emptyList<Matchup>(), useCase.invoke().first().second)
    }

    @Test
    fun `Non empty Matchup List`() = runTest {
        val matchups = listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
        stateFlow.value = true to matchups
        assertEquals(matchups, useCase.invoke().first().second)
    }

    @Test
    fun `Multiple Value Changes`() = runTest {
        useCase.invoke().test {
            awaitItem()
            stateFlow.value = true to listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
            awaitItem()
            stateFlow.value = false to emptyList()
            awaitItem()
        }
    }

    @Test
    fun `Boolean value True Emission`() = runTest {
        stateFlow.value = true to emptyList()
        assertTrue(useCase.invoke().first().first)
    }

    @Test
    fun `Boolean Value False Emission`() = runTest {
        stateFlow.value = false to emptyList()
        assertFalse(useCase.invoke().first().first)
    }

    @Test
    fun `Concurrent access`() = runTest {
        useCase.invoke().test {
            awaitItem()
            repeat(1000) {
                stateFlow.value = (it % 2 == 0) to listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
                awaitItem()
            }
        }
    }

    @Test
    fun `StateFlow is not modified`() = runTest {
        val initial = stateFlow.value
        useCase.invoke()
        assertEquals(initial, stateFlow.value)
    }

    @Test
    fun `Verify flow type`() = runTest {
        val flow = useCase.invoke()
        assertTrue(flow is kotlinx.coroutines.flow.Flow<Pair<Boolean, List<Matchup>>>)
    }

    @Test
    fun `No Exception Thrown`() = runTest {
        useCase.invoke().test {
            assertEquals(false to emptyList<Matchup>(), awaitItem())
        }
    }

    @Test
    fun `Cold Flow Behavior`() = runTest {
        val flow = useCase.invoke()
        stateFlow.value = true to listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
        assertEquals(stateFlow.value, flow.first())
    }
}
