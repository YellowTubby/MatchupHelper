package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.datasources.local.LocalChampion
import com.yellowtubby.victoryvault.data.datasources.local.LocalMatchup
import com.yellowtubby.victoryvault.data.datasources.local.MatchupDAO
import com.yellowtubby.victoryvault.data.datasources.local.MatchupDatabase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals


class MatchupRepositoryImplTest : MockedKoinTest() {

    private lateinit var repository: MatchupRepositoryImpl
    private lateinit var mockMatchupDatabase: MatchupDatabase
    private lateinit var mockMatchupDao: MatchupDAO

    @Before
    fun setUp() {
        // Declare the mock objects
        mockMatchupDatabase = declareMock<MatchupDatabase>()
        mockMatchupDao = declareMock<MatchupDAO>()

        // Mock the database and DAO methods
        coEvery { mockMatchupDatabase.matchupsDao() } returns mockMatchupDao

        // Initialize the repository (Koin will inject the mocks)
        repository = MatchupRepositoryImpl()
    }

    @Test
    fun `upsertChampion should upsert champion in the database`() = runTest {
        // Arrange
        val champion = Champion("Champion1")
        coEvery { mockMatchupDao.upsertChampion(any()) } just Runs

        // Act
        repository.upsertChampion(champion)

        // Assert
        coVerify { mockMatchupDao.upsertChampion(any()) }
    }

    @Test
    fun `upsertMatchup should upsert matchup in the database`() = runTest {
        // Arrange
        val matchup = Matchup(Champion("Champion1"), Champion("Enemy1"), Role.NAN, "", 0, 0, 0)
        coEvery { mockMatchupDao.upsertMatchup(any()) } just Runs

        // Act
        repository.upsertMatchup(matchup)

        // Assert
        coVerify { mockMatchupDao.upsertMatchup(any()) }
    }

    @Test
    fun `getAllMatchups should map local matchups to external matchups`() = runTest {
        // Arrange
        val localMatchups = listOf(
            LocalMatchup("Champion1", "Enemy1", Role.NAN, 0, 0, "", 0)
        )
        val externalMatchups = listOf(
            Matchup(Champion("Champion1"), Champion("Enemy1"), Role.NAN, "", 0, 0, 0)
        )
        coEvery { mockMatchupDao.getAllMatchups() } returns flowOf(localMatchups)

        // Act
        val result = repository.getAllMatchups()

        // Assert
        result.collect { matchups ->
            assertEquals(externalMatchups, matchups)
        }
    }

    @Test
    fun `getAllDefinedChampions should map local champions to external champions`() = runTest {
        // Arrange
        val localChampions = listOf(LocalChampion("Champion1"))
        val externalChampions = listOf(Champion("Champion1"))
        coEvery { mockMatchupDao.getAllChampions() } returns flowOf(localChampions)

        // Act
        val result = repository.getAllDefinedChampions()

        // Assert
        result.collect { champions ->
            assertEquals(externalChampions, champions)
        }
    }

    @Test
    fun `deleteMatchups should delete matchups from the database`() = runTest {
        // Arrange
        val champion = "Champion1"
        val role = Role.NAN
        val matchups = listOf(Matchup(Champion("Champion1"), Champion("Enemy1"), Role.NAN, "", 0, 0, 0))
        coEvery { mockMatchupDao.deleteMatchups(champion, role, any()) } just Runs

        // Act
        repository.deleteMatchups(champion, role, matchups)

        // Assert
        coVerify { mockMatchupDao.deleteMatchups(champion, role, any()) }
    }
}

