package com.example.amphibians


class AmphibiansApplication : Application() {
// App container
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
