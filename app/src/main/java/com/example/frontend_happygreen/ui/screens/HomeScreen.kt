package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.example.frontend_happygreen.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel
import com.example.frontend_happygreen.viewmodel.AuthViewModel

/*@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Benvenuto!", style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo HappyGreen"
        )
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
}*/
@Composable
fun HomeScreen(
    navController: NavController,
    groupViewModel: GroupViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val token by authViewModel.token.collectAsState()
    val mieiGruppi by groupViewModel.mieiGruppi.collectAsState()
    LaunchedEffect(token) {
        token?.let { groupViewModel.loadMyGroups(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Benvenuto!", style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo HappyGreen"
        )

        // Sezione bottoni rapidi
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeIcon(Icons.Default.Add, "Nuovo gruppo") { navController.navigate("group_create") }
            HomeIcon(Icons.Default.QuestionMark, "Quiz") { navController.navigate("quiz") }
            HomeIcon(Icons.Default.Person, "Profilo") { navController.navigate("profile") }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeIcon(
                Icons.Default.CameraAlt,
                "Scanner oggetti"
            ) { navController.navigate("scanner") }
            HomeIcon(
                Icons.Default.QrCodeScanner,
                "Barcode"
            ) { navController.navigate("barcode_scanner") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("I tuoi gruppi", style = MaterialTheme.typography.titleMedium)

        if (mieiGruppi.isEmpty()) {
            Text("Non sei ancora iscritto a nessun gruppo.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                mieiGruppi.forEach { gruppo ->
                    ElevatedCard(
                        onClick = {
                            navController.navigate("group_feed/${gruppo.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(gruppo.nome, style = MaterialTheme.typography.titleMedium)
                            if (gruppo.descrizione.isNotBlank()) {
                                Text(
                                    gruppo.descrizione,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
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