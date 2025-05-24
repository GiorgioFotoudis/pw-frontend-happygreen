package com.example.frontend_happygreen.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import com.example.frontend_happygreen.R
import com.example.frontend_happygreen.ui.theme.*

@Composable
fun SplashScreen(navController: NavController) {
    // Animazioni per gli elementi
    val infiniteTransition = rememberInfiniteTransition(label = "splash_animation")

    // Animazione di pulsazione per il logo
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    // Animazione di rotazione per gli elementi decorativi
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Animazione di fade in per il testo
    val textAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1500, delayMillis = 500),
        label = "text_alpha"
    )

    // Animazione di scala per l'entrata del logo
    val logoEnterScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_enter_scale"
    )

    LaunchedEffect(Unit) {
        delay(3000) // 3 secondi per godersi l'animazione
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EcoGreen50,
                        EcoGreen100,
                        EcoGreen200
                    )
                )
            )
    ) {
        // Elementi decorativi di sfondo
        BackgroundElements(rotation = rotation)

        // Contenuto principale
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo con animazioni
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoEnterScale * logoScale),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo HappyGreen",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Titolo animato
            Text(
                text = "🌱 HappyGreen",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EcoGreen800
                ),
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sottotitolo
            Text(
                text = "La tua app per la sostenibilità",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = EcoGreen600
                ),
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Indicatore di caricamento elegante
            LoadingIndicator(modifier = Modifier.alpha(textAlpha))
        }

        // Testo in basso
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(textAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Prendiamoci cura del nostro pianeta",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = EcoGreen700
                ),
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🌍 ♻️ 🌿",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun BackgroundElements(rotation: Float) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Cerchi decorativi di sfondo
        drawCircle(
            color = EcoGreen100.copy(alpha = 0.3f),
            radius = 150f,
            center = Offset(canvasWidth * 0.2f, canvasHeight * 0.3f)
        )

        drawCircle(
            color = LeafGreen.copy(alpha = 0.2f),
            radius = 100f,
            center = Offset(canvasWidth * 0.8f, canvasHeight * 0.7f)
        )

        drawCircle(
            color = EcoGreen200.copy(alpha = 0.4f),
            radius = 80f,
            center = Offset(canvasWidth * 0.1f, canvasHeight * 0.8f)
        )

        // Elementi rotanti
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(canvasWidth * 0.9f, canvasHeight * 0.2f)
            canvas.rotate(rotation)

            drawCircle(
                color = TealGreen.copy(alpha = 0.3f),
                radius = 60f,
                center = Offset.Zero
            )

            canvas.restore()
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val dotScale1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dotScale2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dotScale3 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            val scale = when (index) {
                0 -> dotScale1
                1 -> dotScale2
                else -> dotScale3
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .scale(scale)
                    .background(
                        color = EcoGreen500,
                        shape = CircleShape
                    )
            )
        }
    }
}