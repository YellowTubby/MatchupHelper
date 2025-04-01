package com.yellowtubby.victoryvault.domain.matchups

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.repositories.MatchupRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RemoveMatchUpsUseCaseTest : MockedKoinTest() {

    private lateinit var matchupRepository: MatchupRepository
    private lateinit var useCase: RemoveMatchUpsUseCase

    @Before
    fun setUp() {
        // Mock the repository using MockK
        matchupRepository = declareMock<MatchupRepository>()
        useCase = RemoveMatchUpsUseCase()
    }

    @Test
    fun `Successful deletion of matchups`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = listOf(
            Matchup(orig = champion, enemy = Champion("Nasus"))
        )

        // Mock the suspend function to return Unit (indicating successful deletion)
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } returns Unit

        // Invoke the use case with Champion object
        useCase.invoke(champion, role, matchups)

        // Verify that deleteMatchups was called with correct arguments
        coVerify { matchupRepository.deleteMatchups(champion.name, role, matchups) }
    }

    @Test
    fun `Empty matchups list`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = emptyList<Matchup>()

        // Mock the suspend function to return Unit (indicating successful deletion)
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } returns Unit

        // Invoke the use case with an empty list
        useCase.invoke(champion, role, matchups)

        // Verify that deleteMatchups was called with correct arguments
        coVerify { matchupRepository.deleteMatchups(champion.name, role, matchups) }
    }

    @Test
    fun `Empty champion name`() = runTest {
        val champion = Champion("") // Empty name
        val role = Role.TOP
        val matchups = listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")))

        // Simulate a scenario where champion name is empty
        assertFailsWith<IllegalArgumentException> {
            useCase.invoke(champion, role, matchups)
        }
    }

    @Test
    fun `Repository throws exception`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")))

        // Simulate an exception being thrown from the repository
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } throws Exception("Deletion failed")

        // Verify that the exception is propagated or handled correctly
        assertFailsWith<Exception> {
            useCase.invoke(champion, role, matchups)
        }
    }

    @Test
    fun `Large matchups list`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = List(1000) { Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")) }

        // Mock the suspend function to return Unit (indicating successful deletion)
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } returns Unit

        // Invoke the use case with a large list
        useCase.invoke(champion, role, matchups)

        // Verify that deleteMatchups was called with the large list
        coVerify { matchupRepository.deleteMatchups(champion.name, role, matchups) }
    }

    @Test
    fun `Valid champion object with role and matchups`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = listOf(Matchup(orig = champion, enemy = Champion("Nasus")))

        // Mock the suspend function to return Unit (indicating successful deletion)
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } returns Unit

        // Invoke the use case with valid inputs
        useCase.invoke(champion, role, matchups)

        // Verify that deleteMatchups was called with correct arguments
        coVerify { matchupRepository.deleteMatchups(champion.name, role, matchups) }
    }

    @Test
    fun `Duplicate matchups in list`() = runTest {
        val champion = Champion("Aatrox")
        val role = Role.TOP
        val matchups = listOf(
            Matchup(orig = champion, enemy = Champion("Nasus")),
            Matchup(orig = champion, enemy = Champion("Nasus")) // Duplicate
        )

        // Mock the suspend function to return Unit (indicating successful deletion)
        coEvery { matchupRepository.deleteMatchups(champion.name, role, matchups) } returns Unit

        // Invoke the use case with duplicate matchups
        useCase.invoke(champion, role, matchups)

        // Verify that deleteMatchups was called with correct arguments (duplicates included)
        coVerify { matchupRepository.deleteMatchups(champion.name, role, matchups) }
    }

    @Test
    fun `Incorrect champion type`() = runTest {
        // Test for incorrect champion type (assuming it's part of the flow)
        val champion: Any = 123 // Invalid type
        val role = Role.TOP
        val matchups = listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")))

        assertFailsWith<ClassCastException> {
            useCase.invoke(champion as Champion, role, matchups)
        }
    }

    @Test
    fun `Incorrect role type`() = runTest {
        // Test for incorrect role type (assuming it's part of the flow)
        val champion = Champion("Aatrox")
        val role: Any = 123 // Invalid type
        val matchups = listOf(Matchup(orig = Champion("Aatrox"), enemy = Champion("Nasus")))

        assertFailsWith<ClassCastException> {
            useCase.invoke(champion, role as Role, matchups)
        }
    }
}
