package com.sai.flashcardai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Warm, inviting palette — amber + charcoal + cream
val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Amber600 = Color(0xFFD97706)
val Charcoal = Color(0xFF1C1917)
val CharcoalLight = Color(0xFF292524)
val Cream = Color(0xFFFFFBEB)
val CreamDark = Color(0xFFFEF3C7)
val Sage = Color(0xFF059669)
val SageLight = Color(0xFF6EE7B7)
val Coral = Color(0xFFEF4444)
val CoralLight = Color(0xFFFCA5A5)
val SlateText = Color(0xFF44403C)
val SlateSubtle = Color(0xFF78716C)

private val DarkColorScheme = darkColorScheme(
    primary = Amber400,
    onPrimary = Charcoal,
    primaryContainer = Amber600,
    secondary = SageLight,
    onSecondary = Charcoal,
    background = Charcoal,
    surface = CharcoalLight,
    onBackground = Cream,
    onSurface = Cream,
    error = CoralLight,
    onError = Charcoal,
)

private val LightColorScheme = lightColorScheme(
    primary = Amber500,
    onPrimary = Color.White,
    primaryContainer = CreamDark,
    secondary = Sage,
    onSecondary = Color.White,
    background = Cream,
    surface = Color.White,
    onBackground = Charcoal,
    onSurface = SlateText,
    error = Coral,
    onError = Color.White,
)

@Composable
fun FlashcardAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = TextStyle(
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                letterSpacing = (-1).sp,
                lineHeight = 38.sp
            ),
            headlineMedium = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp,
                lineHeight = 30.sp
            ),
            titleLarge = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                letterSpacing = 0.sp
            ),
            titleMedium = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 0.1.sp
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp
            ),
            bodyMedium = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            labelLarge = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 0.5.sp
            )
        ),
        content = content
    )
}
