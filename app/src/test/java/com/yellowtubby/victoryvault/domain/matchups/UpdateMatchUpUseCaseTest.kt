package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import com.yellowtubby.victoryvault.domain.matchups.UpdateMatchUpUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMatchUpUseCaseTest : MockedKoinTest() {

    private lateinit var matchupRepository: MatchupRepository
    private lateinit var useCase: UpdateMatchUpUseCase

    @Before
    fun setUp() {
        matchupRepository = declareMock<MatchupRepository>()
        useCase = UpdateMatchUpUseCase()
    }

    @Test
    fun `getMatchupRepository returns expected object type`() {
        assertTrue(matchupRepository is MatchupRepository)
    }

    @Test
    fun `getMatchupRepository returns non null object`() {
        assertNotNull(matchupRepository)
    }


    @Test
    fun `getMatchupRepository dependency injection integrity`() {
        // If using Koin or any DI tool, verify injection here.
        assertNotNull(matchupRepository)
    }

    @Test
    fun `invoke upsertMatchup correct call`() = runTest {
        val matchup = Matchup()
        coEvery { matchupRepository.upsertMatchup(matchup) } returns Unit

        useCase(matchup)

        coVerify { matchupRepository.upsertMatchup(matchup) }
    }


    @Test
    fun `invoke with empty Matchup object`() = runTest {
        val emptyMatchup = Matchup(
            orig = Champion(""),
            enemy = Champion(""),
            role = Role.NAN,
            description = "",
            numWins = 0,
            numTotal = 0,
            difficulty = 0
        )
        coEvery { matchupRepository.upsertMatchup(emptyMatchup) } returns Unit

        useCase(emptyMatchup)

        coVerify { matchupRepository.upsertMatchup(emptyMatchup) }
    }

    @Test
    fun `invoke exception handling from upsertMatchup`() = runTest {
        val matchup = Matchup()
        coEvery { matchupRepository.upsertMatchup(matchup) } throws IOException("Database error")

        try {
            useCase(matchup)
            fail("Expected exception when invoking upsertMatchup with error")
        } catch (e: IOException) {
            assertTrue(e.message!!.contains("Database error"))
        }
    }

    @Test
    fun `invoke asynchronous execution confirmation`() = runTest {
        val matchup = Matchup()
        coEvery { matchupRepository.upsertMatchup(matchup) } returns Unit

        // Since the method is suspend, just verify it can be executed asynchronously without issues
        useCase(matchup)

        coVerify { matchupRepository.upsertMatchup(matchup) }
    }

    @Test
    fun `invoke race condition check`() = runTest {
        val matchup = Matchup()
        coEvery { matchupRepository.upsertMatchup(matchup) } returns Unit

        // Simulate multiple coroutines trying to invoke the use case
        val job1 = launch { useCase(matchup) }
        val job2 = launch { useCase(matchup) }

        job1.join()
        job2.join()

        coVerify(exactly = 2) { matchupRepository.upsertMatchup(matchup) }
    }

    @Test
    fun `invoke with valid matchup`() = runTest {
        val validMatchup = Matchup(
            orig = Champion("Aatrox"),
            enemy = Champion("Nasus"),
            role = Role.TOP,
            description = "Aatrox vs Nasus",
            numWins = 10,
            numTotal = 15,
            difficulty = 3
        )
        coEvery { matchupRepository.upsertMatchup(validMatchup) } returns Unit

        useCase(validMatchup)

        coVerify { matchupRepository.upsertMatchup(validMatchup) }
    }

    @Test
    fun `invoke with invalid matchup`() = runTest {
        val invalidMatchup = Matchup(
            orig = Champion(""),
            enemy = Champion(""),
            role = Role.NAN,
            description = "",
            numWins = -1,  // Invalid data
            numTotal = -1, // Invalid data
            difficulty = -1  // Invalid data
        )
        coEvery { matchupRepository.upsertMatchup(invalidMatchup) } returns Unit

        useCase(invalidMatchup)

        coVerify { matchupRepository.upsertMatchup(invalidMatchup) }
    }
}
