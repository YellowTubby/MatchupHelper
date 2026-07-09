package com.serj.matchuphelper

import android.app.Application
import com.serj.matchuphelper.di.commonModule
import com.serj.matchuphelper.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MatchupHelperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MatchupHelperApp)
            modules(platformModule(BuildConfig.GEMINI_API_KEY), commonModule)
        }
    }
}
