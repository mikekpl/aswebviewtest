package com.mikelau.aswebviewtest

import android.app.Application
import com.mikelau.aswebview.network.asWebViewAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

open class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    asWebViewAppModule
                )
            )
        }
    }
}