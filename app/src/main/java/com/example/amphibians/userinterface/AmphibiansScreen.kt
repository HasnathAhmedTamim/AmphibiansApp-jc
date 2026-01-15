package com.example.amphibians.userinterface

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.amphibians.data.Amphibian



//Top-level composable that switches UI based on uiState: shows loading, error (with retry callback), or the list of amphibians.
@Composable
fun AmphibiansApp(
    uiState: AmphibiansUiState,
    onRetry: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is AmphibiansUiState.Loading -> LoadingScreen()
            is AmphibiansUiState.Error -> ErrorScreen(onRetry)
            is AmphibiansUiState.Success -> AmphibiansList(uiState.amphibians)
        }
    }
}


//Displays a centered CircularProgressIndicator while data is loading.

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

//Shows an error message and a Retry button; calls onRetry when the button is pressed to reattempt loading.

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Failed to load amphibians")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
//Displays a scrolling LazyColumn of amphibian cards; adds spacing between items.

@Composable
fun AmphibiansList(amphibians: List<Amphibian>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(amphibians) { amphibian ->
            AmphibianCard(amphibian)
            Spacer(Modifier.height(8.dp))
        }
    }
}


//Presents a single amphibian inside a Card: title (name + type), image loaded via Coil, and description; uses Material typography and padding for layout.

@Composable
fun AmphibianCard(amphibian: Amphibian) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${amphibian.name} (${amphibian.type})",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(amphibian.imgSrc),
                contentDescription = amphibian.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = amphibian.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}