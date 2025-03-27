package com.yellowtubby.victoryvault.core.di

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    val io : CoroutineDispatcher
    val ui : CoroutineDispatcher
    val default: CoroutineDispatcher
}