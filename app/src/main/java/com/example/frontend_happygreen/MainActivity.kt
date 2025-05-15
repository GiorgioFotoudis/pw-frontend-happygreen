package com.example.frontend_happygreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.frontend_happygreen.ui.screens.*
import com.example.frontend_happygreen.ui.theme.FrontendhappygreenTheme
import com.example.frontendhappygreen.ui.screens.ScannerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrontendhappygreenTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("group_create") { GroupCreateScreen() }
                    composable("quiz") { QuizScreen() }
                    composable("profile") { ProfileScreen() }
                    composable("scanner") { ScannerScreen() }
                    composable("barcode_scanner") { BarcodeScannerScreen() }
                }
            }
        }
    }
}
