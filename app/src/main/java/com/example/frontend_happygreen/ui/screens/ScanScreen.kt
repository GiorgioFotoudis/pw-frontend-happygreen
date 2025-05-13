package com.example.frontendhappygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend_happygreen.ui.components.CameraPreview

@Composable
fun ScanScreen(
    onObjectConfirmed: (String) -> Unit = {}
) {
    var recognizedLabel by remember { mutableStateOf("Nessun oggetto riconosciuto") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Fotocamera
        CameraPreview(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onResult = { result ->
                recognizedLabel = result
            }
        )

        // Risultato in tempo reale
        Text(
            text = recognizedLabel,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
        )

        // Bottone per "confermare" e magari inviare al backend
        Button(
            onClick = {
                onObjectConfirmed(recognizedLabel)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Scatta e conferma")
        }
    }
}
