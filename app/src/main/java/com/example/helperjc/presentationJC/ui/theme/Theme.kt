package com.example.helperjc.presentationJC.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.helperjc.ui.theme.BackgroundDark
import com.example.helperjc.ui.theme.BackgroundLight
import com.example.helperjc.ui.theme.ButtonColorDark
import com.example.helperjc.ui.theme.ButtonColorLight
import com.example.helperjc.ui.theme.ButtonSecondaryDark
import com.example.helperjc.ui.theme.ButtonSecondaryLight
import com.example.helperjc.ui.theme.SurfaceDark
import com.example.helperjc.ui.theme.SurfaceLight
import com.example.helperjc.ui.theme.SurfaceVariantDark
import com.example.helperjc.ui.theme.SurfaceVariantLight
import com.example.helperjc.ui.theme.TextDisabledDark
import com.example.helperjc.ui.theme.TextDisabledLight
import com.example.helperjc.ui.theme.TextPrimaryDark
import com.example.helperjc.ui.theme.TextPrimaryLight

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,

    primary = ButtonColorDark,
    secondary = ButtonSecondaryDark,

    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onPrimary = Color.White,

    outline = TextDisabledDark
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,

    primary = ButtonColorLight,
    secondary = ButtonSecondaryLight,

    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onPrimary = Color.Black,

    outline = TextDisabledLight

)

@Composable
fun HelperJCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = true,
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
        typography = Typography(),
        content = content
    )
}