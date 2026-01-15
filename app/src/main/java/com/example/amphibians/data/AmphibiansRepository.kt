package com.example.amphibians.data

import com.example.amphibians.network.AmphibiansApi

interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

class NetworkAmphibiansRepository : AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibian> {
        return AmphibiansApi.service.getAmphibians()
    }
}