package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.frontend_happygreen.ui.components.CameraPreview

@Composable
fun ScannerScreen(
    onObjectConfirmed: (String) -> Unit = {}
) {
    var recognizedLabel by remember { mutableStateOf("Nessun oggetto riconosciuto") }
    var classification by remember { mutableStateOf("Inclassificato") }
    var color by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Fotocamera
        CameraPreview(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onResult = { result ->
                recognizedLabel = result
                val cleaned = result.lowercase()
                classification = when {
                    "plastic" in cleaned || "bottle" in cleaned -> "Plastica"
                    "paper" in cleaned || "book" in cleaned -> "Carta"
                    "glass" in cleaned -> "Vetro"
                    "metal" in cleaned || "can" in cleaned -> "Metallo"
                    "organic" in cleaned || "food" in cleaned -> "Organico"
                    else -> "Altro"
                }
                color = when (classification) {
                    "Plastica" -> Color(0xFF64B5F6)
                    "Carta" -> Color(0xFFFFF176)
                    "Vetro" -> Color(0xFF81C784)
                    "Metallo" -> Color(0xFF90A4AE)
                    "Organico" -> Color(0xFFA1887F)
                    else -> Color.Gray
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Etichetta oggetto riconosciuto
        Text(
            text = "Oggetto: $recognizedLabel",
            style = MaterialTheme.typography.bodyLarge
        )

        // Categoria rifiuto
        Text(
            text = "Categoria: $classification",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color)
                .padding(12.dp),
            style = MaterialTheme.typography.titleMedium
        )

        // Bottone di conferma
        Button(
            onClick = {
                onObjectConfirmed(classification)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Conferma classificazione")
        }
    }
}
