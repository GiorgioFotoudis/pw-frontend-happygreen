package com.example.frontend_happygreen

import com.example.frontend_happygreen.ui.screens.NewPostScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.frontend_happygreen.ui.screens.*
import com.example.frontend_happygreen.ui.theme.FrontendhappygreenTheme
import com.example.frontend_happygreen.ui.screens.ScannerScreen
import com.example.frontend_happygreen.viewmodel.AuthViewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrontendhappygreenTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val groupViewModel: GroupViewModel = viewModel()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController, authViewModel) }
                    composable("register") { RegisterScreen(navController, authViewModel) }
                    composable("home") { HomeScreen(navController, groupViewModel, authViewModel) }
                    composable("group_create") {
                        val token = authViewModel.token.collectAsState().value
                        token?.let {
                            GroupCreateFormScreen(token = it) {
                                navController.navigate("home")
                            }
                        } ?: Text("Token non disponibile")
                    }
                    composable("quiz") {
                        val token = authViewModel.token.collectAsState().value
                        token?.let {
                            QuizScreen(navController = navController, token = it)
                        } ?: Text("Token non disponibile")
                    }
                    composable("profile") { ProfileScreen(navController, authViewModel) }
                    composable("scanner") { ScannerScreen() }
                    composable("barcode_scanner") { BarcodeScannerScreen() }
                    composable("group_feed/{groupId}",
                        arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val gruppoId = backStackEntry.arguments?.getInt("groupId") ?: return@composable
                        val token = authViewModel.token.collectAsState().value ?: return@composable

                        GroupFeedScreen(
                            gruppoId = gruppoId,
                            token = token,
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                    composable("group_create_post/{groupId}",
                        arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val gruppoId = backStackEntry.arguments?.getInt("groupId") ?: return@composable
                        val tokenState = authViewModel.token.collectAsState()
                        val token = tokenState.value

                        if (token == null) {
                            Text("Attendi caricamento token...")
                        } else {
                            NewPostScreen(
                                gruppoId = gruppoId,
                                token = token,
                                navController = navController
                            )
                        }
                    }
                    composable("group_search") {
                        val token = authViewModel.token.collectAsState().value
                        token?.let {
                            GroupSearchScreen(navController = navController, token = it)
                        } ?: Text("Token non disponibile")
                    }
                }
            }
        }
    }
}