package com.serj.matchuphelper

import androidx.compose.ui.window.ComposeUIViewController
import com.serj.matchuphelper.di.commonModule
import com.serj.matchuphelper.di.platformModule
import org.koin.core.context.startKoin

fun initKoin(geminiApiKey: String) {
    startKoin {
        modules(platformModule(geminiApiKey), commonModule)
    }
}

fun MainViewController() = ComposeUIViewController { App() }
