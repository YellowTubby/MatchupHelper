package com.yellowtubby.victoryvault.domain.userdata

import app.cash.turbine.Event
import app.cash.turbine.test
import com.yellowtubby.victoryvault.data.datamodels.Role
import com.yellowtubby.victoryvault.data.datamodels.UserData
import com.yellowtubby.victoryvault.data.repositories.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserDataUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = GetCurrentUserDataUseCase(userRepository)

    @Test
    fun `Successful data retrieval`() = runTest {
        val userData = UserData()
        coEvery { userRepository.getCurrentUserData() } returns flowOf(userData)

        useCase().test {
            assertEquals(userData, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Repository emits an empty Flow`() = runTest {
        coEvery { userRepository.getCurrentUserData() } returns emptyFlow()

        useCase().test {
            assertEquals(Event.Complete, awaitEvent())
        }
    }

    @Test
    fun `Repository emits multiple UserData`() = runTest {
        val userData1 = UserData()
        val userData2 = UserData(currentRole = Role.TOP)
        coEvery { userRepository.getCurrentUserData() } returns flowOf(userData1, userData2)

        useCase().test {
            assertEquals(userData1, awaitItem())
            assertEquals(userData2, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Repository throws an exception`() = runTest {
        coEvery { userRepository.getCurrentUserData() } returns flow { throw IOException("Network error") }

        useCase().test {
            awaitError()
        }
    }

    @Test
    fun `Check returned data correctness`() = runTest {
        val userData = UserData(currentRole = Role.JUNGLE)
        coEvery { userRepository.getCurrentUserData() } returns flowOf(userData)

        useCase().test {
            assertEquals(userData, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Downstream cancellation handling`() = runTest {
        coEvery { userRepository.getCurrentUserData() } returns flow {
            emit(UserData())
            kotlinx.coroutines.delay(Long.MAX_VALUE)
        }

        useCase().test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Check initial emit`() = runTest {
        val userData = UserData()
        coEvery { userRepository.getCurrentUserData() } returns flowOf(userData)

        useCase().test {
            assertEquals(userData, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Check hot cold flow behaviour`() = runTest {
        var collectorCount = 0
        coEvery { userRepository.getCurrentUserData() } returns flow {
            collectorCount++
            emit(UserData())
        }

        useCase().test {
            assertEquals(1, collectorCount)
            awaitItem()
            awaitComplete()
        }
    }
}
