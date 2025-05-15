package com.example.frontend_happygreen.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val lightColorPalette = lightColorScheme(
    primary = Green500,        // Verde principale
    onPrimary = Color.White,   // Testo su primary
    secondary = Green200,      // Verde chiaro
    onSecondary = Color.Black, // Testo su secondary
    background = LightGreen,   // Colore di sfondo chiaro
    onBackground = Color.Black,
    surface = Color.White,     // Superficie bianca
    onSurface = DarkGreen,     // Testo scuro sulla superficie
    error = Color.Red,         // Colore per errori
    onError = Color.White      // Testo su errore
)

val darkColorPalette = darkColorScheme(
    primary = Green500,
    onPrimary = Color.Black,
    secondary = Green200,
    onSecondary = Color.Black,
    background = DarkGreen,    // Sfondi scuri
    onBackground = Color.White,
    surface = DarkGreen,
    onSurface = Color.White,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun FrontendhappygreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorPalette
        else -> lightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}