package com.yellowtubby.victoryvault.core

import io.mockk.mockkClass
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

open class MockedKoinTest(
    val module: Module = module {}
) : KoinTest {
    @get:Rule
    val mockProvider = MockProviderRule.Companion.create { clazz ->
        mockkClass(clazz)
    }

    @get:Rule
    val koinTestRule = KoinTestRule.Companion.create {
        modules(
            module
        )
    }

}