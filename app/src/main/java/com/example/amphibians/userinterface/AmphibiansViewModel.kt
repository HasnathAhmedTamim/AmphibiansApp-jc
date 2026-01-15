package com.example.amphibians.userinterface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.amphibians.data.AmphibiansRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AmphibiansViewModel(
    private val repository: AmphibiansRepository
) : ViewModel() {

    var uiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    init {
        getAmphibians()
    }

    fun getAmphibians() {
        uiState = AmphibiansUiState.Loading
        viewModelScope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    repository.getAmphibians()
                }
                uiState = AmphibiansUiState.Success(list)
            } catch (e: Exception) {
                uiState = AmphibiansUiState.Error
            }
        }
    }

    companion object {
        // ViewModelFactory manual DI জন্য
        fun provideFactory(
            repository: AmphibiansRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AmphibiansViewModel::class.java)) {
                    return AmphibiansViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}