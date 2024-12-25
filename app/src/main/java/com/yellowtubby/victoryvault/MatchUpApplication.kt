package com.yellowtubby.victoryvault

import android.app.Application
import com.yellowtubby.victoryvault.di.matchUpModule
import com.yellowtubby.victoryvault.domain.GetAllChampionsUseCase
import com.yellowtubby.victoryvault.domain.GetCurrentUserDataUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject

class MatchUpApplication : Application() {

    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase by inject(
        GetCurrentUserDataUseCase::class.java)
    private val getAllChampionsUseCase: GetAllChampionsUseCase by inject(GetAllChampionsUseCase::class.java)


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
        var applicationScope = MainScope()
    }

}