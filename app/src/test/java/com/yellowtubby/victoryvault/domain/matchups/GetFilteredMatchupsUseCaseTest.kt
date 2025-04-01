package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetFilteredMatchupsUseCaseTest : MockedKoinTest() {

    private lateinit var getFilteredMatchupsUseCase: GetFilteredMatchupsUseCase
    private lateinit var matchupRepository: MatchupRepository

    @Before
    fun setup() {
        matchupRepository = declareMock<MatchupRepository>()
        getFilteredMatchupsUseCase = GetFilteredMatchupsUseCase()
    }

    @Test
    fun `Successful retrieval of matchups`() = runTest {
        val matchups = listOf(
            Matchup(Champion("Aatrox"), Champion("Darius")),
            Matchup(Champion("Aatrox"), Champion("Nasus"))
        )
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(matchups)

        val result = getFilteredMatchupsUseCase().first()
        assertEquals(matchups, result)
    }

    @Test
    fun `Empty matchup list`() = runTest {
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(emptyList())

        val result = getFilteredMatchupsUseCase().first()
        assertEquals(emptyList(), result)
    }

    @Test
    fun `Repository throws exception`() = runTest {
        coEvery { matchupRepository.getAllMatchups() } returns flow {
            throw RuntimeException("Repository failure")
        }

        assertFailsWith<RuntimeException> { getFilteredMatchupsUseCase().first() }
    }

    @Test
    fun `Multiple matchups retrieval`() = runTest {
        val matchups1 = listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
        val matchups2 = listOf(Matchup(Champion("Aatrox"), Champion("Darius")))
        coEvery { matchupRepository.getAllMatchups() } returns flow {
            emit(matchups1)
            emit(matchups2)
        }

        val results = getFilteredMatchupsUseCase().toList()
        assertEquals(listOf(matchups1, matchups2), results)
    }

    @Test
    fun `Matchup data validation`() = runTest {
        val matchup = Matchup(Champion("Aatrox"), Champion("Nasus"), Role.TOP, "Strong lane", 5, 10, 3)
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(listOf(matchup))

        val result = getFilteredMatchupsUseCase().first()
        assertEquals(1, result.size)
        assertEquals("Aatrox", result[0].orig.name)
        assertEquals("Nasus", result[0].enemy.name)
        assertEquals(Role.TOP, result[0].role)
        assertEquals("Strong lane", result[0].description)
    }

    @Test
    fun `Flow emissions check`() = runTest {
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(emptyList())
        val flow = getFilteredMatchupsUseCase()
        assertEquals(emptyList(), flow.first())
    }

    @Test
    fun `Null Matchup List`() = runTest {
        coEvery { matchupRepository.getAllMatchups() } returns flowOf()
        val result = getFilteredMatchupsUseCase().first()
        assertEquals(emptyList(), result)
    }

    @Test
    fun `Matchup Repository not injected`() {
        assertFailsWith<UninitializedPropertyAccessException> { GetFilteredMatchupsUseCase() }
    }

    @Test
    fun `Matchup List Size Boundary`() = runTest {
        val singleMatchup = listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(singleMatchup)

        val result = getFilteredMatchupsUseCase().first()
        assertEquals(1, result.size)
    }

    @Test
    fun `Matchup data is mutable`() = runTest {
        val matchup = Matchup(Champion("Aatrox"), Champion("Nasus"))
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(listOf(matchup))

        val result = getFilteredMatchupsUseCase().first()
        assertEquals("Aatrox", result[0].orig.name)
    }

    @Test
    fun `Verify Flow correctness`() = runTest {
        val matchups = listOf(Matchup(Champion("Aatrox"), Champion("Nasus")))
        coEvery { matchupRepository.getAllMatchups() } returns flowOf(matchups)

        val flow = getFilteredMatchupsUseCase()
        assertEquals(matchups, flow.first())
    }
}
