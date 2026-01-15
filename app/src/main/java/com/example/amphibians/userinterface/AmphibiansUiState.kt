package com.example.amphibians.userinterface


import com.example.amphibians.data.Amphibian

sealed interface AmphibiansUiState {
    object Loading : AmphibiansUiState
    data class Success(val amphibians: List<Amphibian>) : AmphibiansUiState
    object Error : AmphibiansUiState
}