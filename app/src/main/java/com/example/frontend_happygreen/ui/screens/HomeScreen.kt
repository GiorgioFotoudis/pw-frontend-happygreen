package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(
                onClick = { navController.navigate("group_create") }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi gruppo")
            }
            IconButton(onClick = { navController.navigate("quiz") }) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Quiz")//icona da cambiare
            }
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(Icons.Default.Person, contentDescription = "Profilo")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { navController.navigate("scanner") }) {
                Icon(Icons.Default.Search, contentDescription = "Scanner")//icona da cambiare
            }
            IconButton(onClick = { navController.navigate("barcode_scanner") }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Codice a barre")//icona da cambiare
            }
        }
    }
}

