package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.example.frontend_happygreen.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel
import com.example.frontend_happygreen.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            // Header di benvenuto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🌱 Benvenuto su HappyGreen!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Esplora, scansiona e partecipa alla community green",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            // Sezione azioni rapide
            Text(
                "Azioni Rapide",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Prima riga
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ModernActionButton(
                            icon = Icons.Default.Add,
                            label = "Nuovo\nGruppo",
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            onClick = { navController.navigate("group_create") }
                        )
                        ModernActionButton(
                            icon = Icons.Default.Search,
                            label = "Trova\nGruppi",
                            backgroundColor = MaterialTheme.colorScheme.tertiary,
                            onClick = { navController.navigate("group_search") }
                        )
                        ModernActionButton(
                            icon = Icons.Default.Quiz,
                            label = "Quiz\nVerde",
                            backgroundColor = Color(0xFF4CAF50),
                            onClick = { navController.navigate("quiz") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Segunda riga
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ModernActionButton(
                            icon = Icons.Default.CameraAlt,
                            label = "Scanner\nOggetti",
                            backgroundColor = Color(0xFF2196F3),
                            onClick = { navController.navigate("scanner") }
                        )
                        ModernActionButton(
                            icon = Icons.Default.QrCodeScanner,
                            label = "Scansiona\nBarcode",
                            backgroundColor = Color(0xFFFF9800),
                            onClick = { navController.navigate("barcode_scanner") }
                        )
                        ModernActionButton(
                            icon = Icons.Default.Person,
                            label = "Il Mio\nProfilo",
                            backgroundColor = Color(0xFF9C27B0),
                            onClick = { navController.navigate("profile") }
                        )
                    }
                }
            }
        }

        item {
            // Sezione gruppi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "I Tuoi Gruppi",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                if (mieiGruppi.isNotEmpty()) {
                    TextButton(
                        onClick = { navController.navigate("my_groups") }
                    ) {
                        Text("Vedi tutti")
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        if (mieiGruppi.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Non sei ancora iscritto a nessun gruppo",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Trova e unisciti a gruppi che condividono i tuoi interessi green!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("group_search") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Trova Gruppi")
                        }
                    }
                }
            }
        } else {
            items(mieiGruppi.take(3)) { gruppo -> // Mostra solo i primi 3 gruppi
                ElevatedCard(
                    onClick = {
                        navController.navigate("group_feed/${gruppo.id}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icona del gruppo
                        Card(
                            modifier = Modifier.size(48.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    gruppo.nome.firstOrNull()?.toString()?.uppercase() ?: "G",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Informazioni gruppo
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                gruppo.nome,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (gruppo.descrizione.isNotBlank()) {
                                Text(
                                    gruppo.descrizione,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    maxLines = 2
                                )
                            }
                        }

                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Padding finale
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ModernActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = backgroundColor.copy(alpha = 0.2f),
                contentColor = backgroundColor
            )
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}