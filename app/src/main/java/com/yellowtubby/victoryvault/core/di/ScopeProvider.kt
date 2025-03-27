package com.yellowtubby.victoryvault.core.di

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    val scope : CoroutineScope?
}