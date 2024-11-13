package com.yellowtubby.victoryvault

import android.app.Application
import com.yellowtubby.victoryvault.di.matchUpModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MatchUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(applicationContext)
            modules(matchUpModule)
        }
    }

    companion object {
        lateinit var instance: MatchUpApplication
            private set
    }

}