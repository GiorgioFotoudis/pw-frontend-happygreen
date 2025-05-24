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
    // Colori principali
    primary = EcoGreen600,              // Verde vivace per elementi principali
    onPrimary = Color.White,            // Testo bianco su primary
    primaryContainer = EcoGreen100,     // Container primary chiaro
    onPrimaryContainer = EcoGreen800,   // Testo scuro su container primary

    // Colori secondari
    secondary = LeafGreen,              // Verde foglia per elementi secondari
    onSecondary = Color.White,          // Testo bianco su secondary
    secondaryContainer = EcoGreen50,    // Container secondary molto chiaro
    onSecondaryContainer = EcoGreen700, // Testo scuro su container secondary

    // Colori terziari per varietà
    tertiary = TealGreen,               // Verde-blu per diversità
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF00695C),

    // Sfondi
    background = EcoGreen50,            // Sfondo principale molto chiaro
    onBackground = EcoGreen900,         // Testo principale scuro

    // Superfici
    surface = Color.White,              // Superficie bianca per card
    onSurface = EcoGreen800,           // Testo su superficie
    surfaceVariant = EcoGreen100,      // Superficie variante
    onSurfaceVariant = EcoGreen700,    // Testo su superficie variante

    // Bordi e divisori
    outline = EcoGreen300,             // Bordi chiari
    outlineVariant = EcoGreen200,      // Bordi molto chiari

    // Colori semantici
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFB71C1C),

    // Elementi interattivi
    surfaceTint = EcoGreen600,
    inverseSurface = EcoGreen800,
    inverseOnSurface = EcoGreen100,
    inversePrimary = EcoGreen200,
    scrim = Color.Black.copy(alpha = 0.32f)
)

val darkColorPalette = darkColorScheme(
    // Colori principali
    primary = EcoGreen400,              // Verde più chiaro per il dark theme
    onPrimary = EcoGreen900,            // Testo scuro su primary
    primaryContainer = EcoGreen700,     // Container primary scuro
    onPrimaryContainer = EcoGreen100,   // Testo chiaro su container primary

    // Colori secondari
    secondary = LeafGreen,              // Verde foglia mantenuto
    onSecondary = EcoGreen900,          // Testo scuro su secondary
    secondaryContainer = EcoGreen800,   // Container secondary scuro
    onSecondaryContainer = EcoGreen100, // Testo chiaro su container secondary

    // Colori terziari
    tertiary = Color(0xFF4DB6AC),       // Teal più chiaro per dark
    onTertiary = Color(0xFF00363A),
    tertiaryContainer = Color(0xFF00695C),
    onTertiaryContainer = Color(0xFFB2DFDB),

    // Sfondi scuri
    background = Color(0xFF0D1F0D),     // Sfondo molto scuro con tinta verde
    onBackground = EcoGreen100,         // Testo chiaro

    // Superfici scure
    surface = Color(0xFF1A251A),        // Superficie scura con tinta verde
    onSurface = EcoGreen100,           // Testo chiaro su superficie
    surfaceVariant = EcoGreen800,      // Superficie variante scura
    onSurfaceVariant = EcoGreen200,    // Testo su superficie variante

    // Bordi per dark theme
    outline = EcoGreen600,             // Bordi visibili nel dark
    outlineVariant = EcoGreen700,      // Bordi meno visibili

    // Colori semantici per dark
    error = Color(0xFFFF6B6B),         // Rosso più morbido per dark
    onError = Color.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Elementi interattivi dark
    surfaceTint = EcoGreen400,
    inverseSurface = EcoGreen100,
    inverseOnSurface = EcoGreen800,
    inversePrimary = EcoGreen600,
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun FrontendhappygreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disponibile su Android 12+
    dynamicColor: Boolean = false, // Disabilitato per mantenere il tema verde
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