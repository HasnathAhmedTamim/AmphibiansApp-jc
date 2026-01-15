package com.example.amphibians

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.amphibians.ui.theme.AmphibiansTheme
import com.example.amphibians.userinterface.AmphibiansApp
import com.example.amphibians.userinterface.AmphibiansViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AmphibiansViewModel by viewModels {
        val app = application as AmphibiansApplication
        AmphibiansViewModel.provideFactory(app.appContainer.amphibiansRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AmphibiansApp(
                    uiState = viewModel.uiState,
                    onRetry = { viewModel.getAmphibians() }
                )
            }
        }
    }
}