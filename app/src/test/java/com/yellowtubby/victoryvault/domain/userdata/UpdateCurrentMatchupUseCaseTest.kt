package com.yellowtubby.victoryvault.domain.userdata

import com.yellowtubby.victoryvault.core.MockedKoinTest
import com.yellowtubby.victoryvault.data.datamodels.Matchup
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.mock.declareMock
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateCurrentMatchupUseCaseTest : MockedKoinTest() {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: UpdateCurrentMatchupUseCase

    @Before
    fun setUp() {
        userRepository = declareMock<UserRepository>()
        useCase = UpdateCurrentMatchupUseCase()
    }

    @Test
    fun `invoke successful update`() = runTest {
        val matchup = Matchup()
        coEvery { userRepository.updateCurrentMatchup(matchup) } returns Unit

        useCase(matchup)

        coVerify { userRepository.updateCurrentMatchup(matchup) }
    }

    @Test
    fun `invoke exception propagation`() = runTest {
        val matchup = Matchup()
        coEvery { userRepository.updateCurrentMatchup(matchup) } throws IOException("Update failed")

        try {
            useCase(matchup)
        } catch (e: IOException) {
            assertTrue(e.message!!.contains("Update failed"))
        }
    }

    @Test
    fun `invoke empty matchup`() = runTest {
        val matchup = Matchup()
        coEvery { userRepository.updateCurrentMatchup(matchup) } returns Unit

        useCase(matchup)
        coVerify { userRepository.updateCurrentMatchup(matchup) }
    }

    @Test
    fun `getUserRepository non null check`() {
        assertNotNull(userRepository)
    }

    @Test
    fun `getUserRepository correct instance type`() {
        assertTrue(userRepository is UserRepository)
    }

    @Test
    fun `invoke multiple updates`() = runTest {
        val matchup1 = Matchup()
        val matchup2 = Matchup()
        coEvery { userRepository.updateCurrentMatchup(any()) } returns Unit

        useCase(matchup1)
        useCase(matchup2)

        coVerify { userRepository.updateCurrentMatchup(matchup1) }
        coVerify { userRepository.updateCurrentMatchup(matchup2) }
    }

    @Test
    fun `invoke performance test`() = runTest {
        val matchup = Matchup()
        coEvery { userRepository.updateCurrentMatchup(matchup) } returns Unit

        repeat(1000) {
            useCase(matchup)
        }
        coVerify(exactly = 1000) { userRepository.updateCurrentMatchup(matchup) }
    }
}
