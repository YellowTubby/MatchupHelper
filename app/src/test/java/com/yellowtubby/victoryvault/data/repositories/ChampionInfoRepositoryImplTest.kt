package com.yellowtubby.victoryvault.data.repositories

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import android.content.res.Resources
import com.yellowtubby.victoryvault.app.presentation.MatchUpApplication
import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals

class ChampionInfoRepositoryImplTest : MockedKoinTest() {

    private lateinit var repository: ChampionInfoRepositoryImpl
    private lateinit var mockResources: Resources
    private lateinit var mockRawFields: List<Int>

    @Before
    fun setUp() {

        // Register mocks for Resources and R.raw
        mockResources = declareMock<Resources>()
        mockRawFields = declareMock<List<Int>>()

        val mockApplication: MatchUpApplication = mockk()

        // Start Koin manually with necessary modules for testing
        MatchUpApplication.instance = mockApplication
        coEvery { mockApplication.resources } returns mockResources

        every { mockResources.getResourceEntryName(any()) } answers {
            val resourceId = it.invocation.args[0] as Int
            // Simulate the corresponding champion name for each resourceId
            when (resourceId) {
                else -> "UnknownChampion"
            }
        }

        // Initialize the repository
        repository = ChampionInfoRepositoryImpl()
    }

    @Test
    fun `should initialize allChampionStateFlow with champions from raw resources`() = runTest {
        // Act: Fetch the list of champions
        val champions = repository.getAllChampions().first()
        // Assert: Ensure the state flow contains the correct champion names
        assertEquals(168, champions.size)
    }

}
