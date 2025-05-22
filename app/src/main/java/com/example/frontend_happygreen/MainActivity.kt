package com.example.frontend_happygreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.frontend_happygreen.ui.screens.*
import com.example.frontend_happygreen.ui.theme.FrontendhappygreenTheme
import com.example.frontend_happygreen.ui.screens.ScannerScreen
import com.example.frontend_happygreen.viewmodel.AuthViewModel

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
                    composable("register") { RegisterScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("group_create") { GroupCreateFormScreen (
                        token = "finto_token_per_test",
                        onGroupCreated = { navController.navigate("group_create") }) }
                    composable("quiz") { QuizScreen() }
                    composable("profile") { ProfileScreen(navController) }
                    composable("scanner") { ScannerScreen() }
                    composable("barcode_scanner") { BarcodeScannerScreen() }
                    composable("group_feed/{groupId}",
                        arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val gruppoId = backStackEntry.arguments?.getInt("groupId") ?: return@composable
                        val authViewModel: AuthViewModel = viewModel()
                        val token = authViewModel.token.collectAsState().value ?: return@composable

                        GroupFeedScreen(
                            gruppoId = gruppoId,
                            token = token,
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}
