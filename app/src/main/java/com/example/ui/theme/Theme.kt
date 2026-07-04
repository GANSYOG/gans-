package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RybColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color.Black,
    secondary = AccentVariant,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = GrayText,
    error = DangerRed,
    onError = Color.White
)

@Composable
fun RentAnythingTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
      colorScheme = RybColorScheme,
      typography = Typography,
      content = content
  )
}
