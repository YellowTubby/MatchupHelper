package com.yellowtubby.matchuphelper

import android.app.Application
import com.yellowtubby.matchuphelper.di.matchUpModule
import org.koin.core.context.GlobalContext.startKoin

class MatchUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            modules(matchUpModule)
        }
    }

    companion object {
        lateinit var instance: MatchUpApplication
            private set
    }

}