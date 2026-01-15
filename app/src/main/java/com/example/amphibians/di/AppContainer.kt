package com.example.amphibians.di

import com.example.amphibians.data.AmphibiansRepository
import com.example.amphibians.data.NetworkAmphibiansRepository

class AppContainer {
    val amphibiansRepository: AmphibiansRepository by lazy {
        NetworkAmphibiansRepository()
    }
}
