package com.example.coinlog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF1D1D1D),
    onBackground = Color.White,
    onTertiary = Color(0xFFD6FF65),
    primary = Color(0xFFD6FF65),
    onPrimary = Color.Black,
    primaryContainer = Color.Transparent,
    onPrimaryContainer = Color.White,
    secondaryContainer = Color.White,
    onSecondaryContainer = Color.Black,
    secondary = Color(0xFFCFB1FB),
    onSecondary = Color.White,

    )

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    onBackground = Color.Black,
    onTertiary = Color(0xFF33658A),
    primary = Color(0xFF33658A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF5EDED),
    onPrimaryContainer = Color.Black,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.Black,

    secondary = Color(0xFF86BBD8),
    onSecondary = Color.Black,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CoinLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) LightColorScheme
    else DarkColorScheme
//        when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}