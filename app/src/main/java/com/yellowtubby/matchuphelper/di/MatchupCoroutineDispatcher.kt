package com.yellowtubby.matchuphelper.di

import kotlinx.coroutines.CoroutineDispatcher

interface MatchupCoroutineDispatcher {
    val io : CoroutineDispatcher
    val ui : CoroutineDispatcher
    val default: CoroutineDispatcher
}