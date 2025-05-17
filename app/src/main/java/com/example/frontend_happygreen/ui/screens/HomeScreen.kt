package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Benvenuto!", style = MaterialTheme.typography.headlineMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeIcon(
                icon = Icons.Default.Add,
                label = "Nuovo gruppo",
                onClick = { navController.navigate("group_create") }
            )
            HomeIcon(
                icon = Icons.Default.QuestionMark,
                label = "Quiz",
                onClick = { navController.navigate("quiz") }
            )
            HomeIcon(
                icon = Icons.Default.Person,
                label = "Profilo",
                onClick = { navController.navigate("profile") }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeIcon(
                icon = Icons.Default.CameraAlt,
                label = "Scanner oggetti",
                onClick = { navController.navigate("scanner") }
            )
            HomeIcon(
                icon = Icons.Default.QrCodeScanner,
                label = "Barcode",
                onClick = { navController.navigate("barcode_scanner") }
            )
        }
    }
}

@Composable
private fun HomeIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(48.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}