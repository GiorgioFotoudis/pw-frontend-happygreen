package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.navigation.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2500) // 2.5 secondi
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("🌱 HappyGreen", style = MaterialTheme.typography.headlineMedium)
        // Qui poi inserirai il logo
    }
}
