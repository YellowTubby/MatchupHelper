package com.yellowtubby.victoryvault.app.presentation

import android.app.Application
import com.yellowtubby.victoryvault.BuildConfig
import com.yellowtubby.victoryvault.app.di.appModule
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import timber.log.Timber

class MatchUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        GlobalContext.startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
        if(BuildConfig.DEBUG) {
            Timber.Forest.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int, tag: String?, message: String, t: Throwable?
                ) {
                    super.log(priority, "SERJ_$tag", message, t)
                }
            })
        }
    }

    companion object {
        lateinit var instance: MatchUpApplication
            private set
        var applicationScope = MainScope()
    }

}