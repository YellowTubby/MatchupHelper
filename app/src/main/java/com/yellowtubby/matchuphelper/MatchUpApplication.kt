package com.yellowtubby.matchuphelper

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.yellowtubby.matchuphelper.di.matchUpModule
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