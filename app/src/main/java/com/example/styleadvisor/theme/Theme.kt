package com.example.styleadvisor.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CustomColorScheme =
  lightColorScheme(
    primary = TextDarkMaroon,
    secondary = TextMuted,
    background = BackgroundWarmWhite,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDarkMaroon,
    onSurface = TextDarkMaroon,
  )

@Composable
fun StyleAdvisorTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to force our custom UI
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = CustomColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
