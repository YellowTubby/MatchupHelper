package com.yellowtubby.victoryvault

import android.app.Application
import android.os.Build
import com.yellowtubby.victoryvault.di.matchUpModule
import com.yellowtubby.victoryvault.domain.champions.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.userdata.GetCurrentUserDataUseCase
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class MatchUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(applicationContext)
            modules(matchUpModule)
        }
        if(BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
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