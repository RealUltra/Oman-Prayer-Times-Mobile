package com.codealyst.omanprayertimes.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = MosqueGreenDark,
    onPrimary = OnMosqueGreenDark,
    primaryContainer = MosqueGreenContainerDark,
    onPrimaryContainer = OnMosqueGreenContainerDark,
    inversePrimary = MosqueGreenLight,
    secondary = SageDark,
    onSecondary = OnSageDark,
    secondaryContainer = SageContainerDark,
    onSecondaryContainer = OnSageContainerDark,
    tertiary = MosqueGoldDark,
    onTertiary = OnMosqueGoldDark,
    tertiaryContainer = MosqueGoldContainerDark,
    onTertiaryContainer = OnMosqueGoldContainerDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

private val LightColorScheme = lightColorScheme(
    primary = MosqueGreenLight,
    onPrimary = OnMosqueGreenLight,
    primaryContainer = MosqueGreenContainerLight,
    onPrimaryContainer = OnMosqueGreenContainerLight,
    inversePrimary = MosqueGreenDark,
    secondary = SageLight,
    onSecondary = OnSageLight,
    secondaryContainer = SageContainerLight,
    onSecondaryContainer = OnSageContainerLight,
    tertiary = MosqueGoldLight,
    onTertiary = OnMosqueGoldLight,
    tertiaryContainer = MosqueGoldContainerLight,
    onTertiaryContainer = OnMosqueGoldContainerLight,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

@Composable
fun OmanPrayerTimesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
