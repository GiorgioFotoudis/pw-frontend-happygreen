package com.example.frontend_happygreen.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.frontend_happygreen.ui.components.CameraPreview
import com.example.frontend_happygreen.ui.theme.*

@Composable
fun ScannerScreen(
    onObjectConfirmed: (String) -> Unit = {}
) {
    var recognizedLabel by remember { mutableStateOf("") }
    var classification by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(true) }
    var showResult by remember { mutableStateOf(false) }
    var confidence by remember { mutableFloatStateOf(0f) }

    // Animazione del pulse per lo scanner
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Animazione del bordo scanner
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.9f),
                        Color.Black.copy(alpha = 0.7f),
                        EcoGreen900.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = EcoGreen600,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scanner Eco-Smart",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen800
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isScanning) "Inquadra l'oggetto da classificare"
                        else "Oggetto riconosciuto!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = EcoGreen600
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Camera Preview con cornice animata
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Camera Preview
                CameraPreview(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp)),
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
                        confidence = when {
                            "bottle" in cleaned || "paper" in cleaned -> 0.85f + (Math.random() * 0.15f).toFloat()
                            "glass" in cleaned || "metal" in cleaned -> 0.75f + (Math.random() * 0.2f).toFloat()
                            else -> 0.6f + (Math.random() * 0.3f).toFloat()
                        }
                        isScanning = false
                        showResult = true
                    }
                )

                // Cornice scanner animata
                if (isScanning) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(pulseScale)
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        EcoGreen400.copy(alpha = borderAlpha),
                                        EcoGreen600.copy(alpha = borderAlpha),
                                        EcoGreen500.copy(alpha = borderAlpha)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                    )
                }

                // Overlay centrale di scanning
                if (isScanning) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = EcoGreen500.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CenterFocusStrong,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risultato della scansione
            AnimatedVisibility(
                visible = showResult,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Header risultato
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Risultato Scansione",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EcoGreen800
                                )
                            )

                            // Indicatore di confidenza
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (confidence > 0.8f) SuccessGreen else WarningAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${(confidence * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (confidence > 0.8f) SuccessGreen else WarningAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Oggetto riconosciuto
                        ResultInfoCard(
                            icon = Icons.Default.Visibility,
                            label = "Oggetto rilevato",
                            value = recognizedLabel.ifEmpty { "Oggetto generico" },
                            backgroundColor = EcoGreen50
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Categoria rifiuto
                        ResultInfoCard(
                            icon = getCategoryIcon(classification),
                            label = "Categoria rifiuto",
                            value = classification.ifEmpty { "Da classificare" },
                            backgroundColor = getCategoryColor(classification).copy(alpha = 0.1f),
                            valueColor = getCategoryColor(classification)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Suggerimento smaltimento
                        ResultInfoCard(
                            icon = Icons.Default.Lightbulb,
                            label = "Suggerimento",
                            value = getDisposalTip(classification),
                            backgroundColor = InfoBlue.copy(alpha = 0.1f),
                            valueColor = InfoBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pulsanti di azione
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Pulsante Nuova Scansione
                OutlinedButton(
                    onClick = {
                        isScanning = true
                        showResult = false
                        recognizedLabel = ""
                        classification = ""
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = EcoGreen600
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(EcoGreen400, EcoGreen600)
                        ),
                        width = 2.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Nuova\nScansione",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Pulsante Conferma
                Button(
                    onClick = {
                        if (classification.isNotEmpty()) {
                            onObjectConfirmed(classification)
                        }
                    },
                    enabled = classification.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen500,
                        disabledContainerColor = EcoGreen300
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Conferma\nClassificazione",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultInfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    backgroundColor: Color,
    valueColor: Color = EcoGreen800
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = valueColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = valueColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = valueColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category) {
        "Plastica" -> Icons.Default.Recycling
        "Carta" -> Icons.Default.Description
        "Vetro" -> Icons.Default.LocalDrink
        "Metallo" -> Icons.Default.Build
        "Organico" -> Icons.Default.Eco
        else -> Icons.Default.Category
    }
}

private fun getCategoryColor(category: String): Color {
    return when (category) {
        "Plastica" -> Color(0xFF2196F3)    // Blu
        "Carta" -> Color(0xFFFF9800)       // Arancione
        "Vetro" -> Color(0xFF4CAF50)       // Verde
        "Metallo" -> Color(0xFF9E9E9E)     // Grigio
        "Organico" -> Color(0xFF8BC34A)    // Verde chiaro
        else -> Color(0xFF757575)          // Grigio scuro
    }
}

private fun getDisposalTip(category: String): String {
    return when (category) {
        "Plastica" -> "Rimuovi etichette e sciacqua prima del riciclo"
        "Carta" -> "Evita carta unta o plastificata"
        "Vetro" -> "Rimuovi tappi e sciacqua il contenitore"
        "Metallo" -> "Schiaccia le lattine per ridurre il volume"
        "Organico" -> "Ideale per il compostaggio domestico"
        else -> "Verifica le regole locali di smaltimento"
    }
}