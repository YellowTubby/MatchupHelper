package com.yellowtubby.victoryvault.di

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    val scope : CoroutineScope?
}