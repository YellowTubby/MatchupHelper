package com.yellowtubby.victoryvault.data.repositories

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Champion
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.datamodels.UserData
import com.yellowtubby.victoryvault.data.datasources.local.MatchupDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals

class UserRepositoryImplTest : MockedKoinTest(

) {

    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var mockUserDataSharedFlow: MutableStateFlow<UserData>

    @Before
    fun setUp() {
        // Register mocks with Koin for testing
        mockUserDataSharedFlow = MutableStateFlow(UserData())

        // Initialize the UserRepositoryImpl
        userRepository = UserRepositoryImpl()
    }

    @After
    fun tearDown() {
        // Stop Koin after each test to clean up
        stopKoin()
    }

    @Test
    fun `updateRole should update the currentRole in UserData`() = runTest {
        // Arrange: Set initial UserData with a default role
        val initialUserData = UserData(currentRole = Role.NAN)
        mockUserDataSharedFlow.emit(initialUserData)

        val newRole = Role.TOP

        // Act: Call updateRole method
        userRepository.updateRole(newRole)

        // Assert: Verify that the currentRole is updated in the StateFlow
        val result = userRepository.getCurrentUserData().first()
        assertEquals(newRole, result.currentRole)
    }

    @Test
    fun `updateSelectedChampion should update the selectedChampion in UserData`() = runTest {
        // Arrange: Set initial UserData with a default champion
        val initialUserData = UserData(selectedChampion = Champion("Aatrox"))
        mockUserDataSharedFlow.emit(initialUserData)

        val newChampion = Champion("Ahri")

        // Act: Call updateSelectedChampion method
        userRepository.updateSelectedChampion(newChampion)

        // Assert: Verify that the selectedChampion is updated in the StateFlow
        val result = userRepository.getCurrentUserData().first()
        assertEquals(newChampion, result.selectedChampion)
    }

    @Test
    fun `updateCurrentMatchup should update the currentMatchup in UserData`() = runTest {
        // Arrange: Set initial UserData with a default matchup
        val initialUserData = UserData(currentMatchup = Matchup())
        mockUserDataSharedFlow.emit(initialUserData)

        val newMatchup = Matchup(Champion("Aatrox"), Champion("Ahri"), Role.TOP)

        // Act: Call updateCurrentMatchup method
        userRepository.updateCurrentMatchup(newMatchup)

        // Assert: Verify that the currentMatchup is updated in the StateFlow
        val result = userRepository.getCurrentUserData().first()
        assertEquals(newMatchup, result.currentMatchup)
    }

    @Test
    fun `getCurrentUserData should emit initial UserData if empty`() = runTest {
        // Arrange: Ensure the shared flow is empty initially
        mockUserDataSharedFlow.emit(UserData())

        // Act: Call getCurrentUserData
        val result = userRepository.getCurrentUserData().first()

        // Assert: Verify that the initial UserData is emitted correctly
        assertEquals(UserData(), result)
    }
}
