package com.eyther.lumbridge.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

val LightOSRSColors = lightColorScheme(
	background = LightBackground,
	surface = LightSurface,
	primary = LightPrimary,
	onPrimary = LightOnPrimary,
	secondary = LightSecondary,
	onSecondary = LightOnSecondary,
	onSurface = LightOnPrimary,
	secondaryContainer = LightAccent,
	error = Color.Red
)

val DarkOSRSColors = darkColorScheme(
	background = DarkBackground,
	surface = DarkSurface,
	primary = DarkPrimary,
	onPrimary = DarkOnPrimary,
	secondary = DarkSecondary,
	onSecondary = DarkOnSecondary,
	onSurface = DarkOnPrimary,
	secondaryContainer = DarkAccent,
	error = Color.Red
)

@Composable
private fun SetStatusBarColor(colorScheme: ColorScheme,) {
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.surface.toArgb()
		}
	}
}

@Composable
fun LumbridgeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colorScheme = if (darkTheme) DarkOSRSColors else LightOSRSColors
	
	SetStatusBarColor(
		colorScheme = colorScheme
	)
	
	MaterialTheme(
		colorScheme = colorScheme,
		typography = OSRSTypography,
		content = content
	)
}