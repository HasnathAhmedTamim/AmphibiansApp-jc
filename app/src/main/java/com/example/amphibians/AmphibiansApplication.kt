package com.example.amphibians

import android.app.Application
import com.example.amphibians.di.AppContainer

class AmphibiansApplication : Application() {
    // for full app single container
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
