package com.example.frontend_happygreen.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.data.model.GruppoDto
import com.example.frontend_happygreen.viewmodel.GroupViewModel
import com.example.frontend_happygreen.viewmodel.AuthViewModel
import com.example.frontend_happygreen.ui.theme.*

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EcoGreen50,
                        Color.White,
                        EcoGreen50.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Header animato con gradiente e icone fluttuanti
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ) + fadeIn()
                ) {
                    HeroWelcomeCard()
                }
            }

            item {
                // Sezione azioni rapide migliorata
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(800, delayMillis = 300)
                    ) + fadeIn()
                ) {
                    QuickActionsSection(navController)
                }
            }

            item {
                // Header gruppi con animazione
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = null,
                            tint = EcoGreen600,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "I Tuoi Gruppi",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen800
                            )
                        )
                    }
                }
            }

            if (mieiGruppi.isEmpty()) {
                item {
                    // Stato vuoto migliorato
                    EmptyGroupsCard(navController)
                }
            } else {
                items(mieiGruppi.take(3)) { gruppo ->
                    // Card gruppo migliorata con animazioni
                    EnhancedGroupCard(
                        gruppo = gruppo,
                        onClick = { navController.navigate("group_feed/${gruppo.id}") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HeroWelcomeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = EcoGreen200,
                spotColor = EcoGreen300
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box {
            // Sfondo con gradiente sottile
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                EcoGreen100.copy(alpha = 0.6f),
                                LeafGreen.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icone decorative fluttuanti
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingIcon("🌱", -10f)
                    FloatingIcon("🌍", 10f)
                    FloatingIcon("♻️", -5f)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "🌱 Benvenuto su HappyGreen!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen800
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Il tuo viaggio verso la sostenibilità inizia qui.\nEsplora, impara e connettiti con la community green! 🌿",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = EcoGreen600,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Badge motivazionale
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = EcoGreen500.copy(alpha = 0.1f),
                    modifier = Modifier.border(
                        1.dp,
                        EcoGreen500.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                ) {
                    Text(
                        "✨ Ogni piccolo gesto conta per il nostro pianeta",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = EcoGreen700
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingIcon(emoji: String, offsetY: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = offsetY,
        targetValue = offsetY + 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Text(
        text = emoji,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.offset(y = animatedOffset.dp)
    )
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                Icons.Default.Dashboard,
                contentDescription = null,
                tint = EcoGreen600,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Azioni Rapide",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EcoGreen800
                )
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = EcoGreen100
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Prima riga
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ModernActionButton(
                        icon = Icons.Default.Add,
                        label = "Nuovo\nGruppo",
                        backgroundColor = EcoGreen500,
                        onClick = { navController.navigate("group_create") }
                    )
                    ModernActionButton(
                        icon = Icons.Default.Search,
                        label = "Cerca\nGruppi",
                        backgroundColor = TealGreen,
                        onClick = { navController.navigate("group_search") }
                    )
                    ModernActionButton(
                        icon = Icons.Default.Quiz,
                        label = "Quiz\nVerde",
                        backgroundColor = LeafGreen,
                        onClick = { navController.navigate("quiz") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Seconda riga
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ModernActionButton(
                        icon = Icons.Default.CameraAlt,
                        label = "Scanner\nOggetti",
                        backgroundColor = SkyBlue,
                        onClick = { navController.navigate("scanner") }
                    )
                    ModernActionButton(
                        icon = Icons.Default.QrCodeScanner,
                        label = "Barcode\nScanner",
                        backgroundColor = SunYellow,
                        onClick = { navController.navigate("barcode_scanner") }
                    )
                    ModernActionButton(
                        icon = Icons.Default.Person,
                        label = "Profilo\nUtente",
                        backgroundColor = ClayOrange,
                        onClick = { navController.navigate("profile") }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernActionButton(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Surface(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            shape = CircleShape,
            color = backgroundColor.copy(alpha = 0.12f),
            shadowElevation = if (isPressed) 2.dp else 6.dp,
            tonalElevation = 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = 0.2f),
                                backgroundColor.copy(alpha = 0.1f)
                            )
                        )
                    )
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = backgroundColor
                )
            }
        }.also {
            LaunchedEffect(isPressed) {
                if (isPressed) {
                    kotlinx.coroutines.delay(100)
                    isPressed = false
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = EcoGreen700
            ),
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun EmptyGroupsCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = EcoGreen100
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icona animata
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                EcoGreen100,
                                EcoGreen50
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "👥",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Inizia la tua avventura green!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EcoGreen800
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Non sei ancora parte di nessun gruppo.\nUnisciti alla community e scopri persone che condividono la tua passione per l'ambiente! 🌍",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = EcoGreen600,
                    lineHeight = 20.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("group_search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen500
                )
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Esplora Gruppi",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun EnhancedGroupCard(
    gruppo: GruppoDto,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = EcoGreen100
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar del gruppo con gradiente
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(EcoGreen400, EcoGreen600)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    gruppo.nome.firstOrNull()?.toString()?.uppercase() ?: "G",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    gruppo.nome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen800
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    if (gruppo.descrizione.isNotBlank()) gruppo.descrizione else "Gruppo dedicato alla sostenibilità",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = EcoGreen600
                    ),
                    maxLines = 2
                )
            }

            Surface(
                shape = CircleShape,
                color = EcoGreen100,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = EcoGreen600,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}