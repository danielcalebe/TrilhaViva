package edu.ws2027.mc01.am.ui.theme

import android.app.Activity
import android.graphics.fonts.Font
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
import edu.ws2027.mc01.am.data.FontSize

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF5bcb91),
  secondary = Color(0xFFf1a93b),
  background = Color(0xFF0f1a14),
  surface = Color(0xFF234534),
  surfaceVariant = Color(0xFF1a2a20),

  onSurface = Color(0xFFe8f1ec),
  onSurfaceVariant = Color(0xFF8c9992),

  onBackground = Color(0xFFe8f1ec),
  onPrimary = Color(0xFF0f1a14),

  error = Color(0xFFe63739),

)

private val LightColorScheme = lightColorScheme(

  primary = Color(0xFF1f6f4a),
  secondary = Color(0xFFf1a93b),
  background = Color(0xFFf7f5ee),
  surface = Color(0xFFe8f0ed),
  surfaceVariant = Color(0xFFffffff),

  onSurface = Color(0xFF1e1e1e),
  onSurfaceVariant = Color(0xFF9a9a9a),

  onBackground = Color(0xFF1e1e1e),
  onPrimary = Color(0xFFe8f0ed),

  error = Color(0xFFe63739),

)



private val DarkHighContrastColorScheme = darkColorScheme(
  primary = Color(0xFFffe602),
  secondary = Color(0xFFffe602),
  background = Color(0xFF000000),
  surface = Color(0xFF000000),
  surfaceVariant = Color(0xFF2b2403),

  onSurface = Color(0xFFFFFFFF),
  onSurfaceVariant = Color(0xFFb7b7b7),

  onBackground = Color(0xFFFFFFFF),
  onPrimary = Color(0xFF000000),

  error = Color(0xFFe63739),

  )

private val LightHighContrastColorScheme = lightColorScheme(

  primary = Color(0xFF013c97),
  secondary = Color(0xFFf1a93b),
  background = Color(0xFFffffff),
  surface = Color(0xFFffffff),
  surfaceVariant = Color(0xFFffffff),

  onSurface = Color(0xFF010101),
  onSurfaceVariant = Color(0xFF4a4a4a),

  onBackground = Color(0xFF1e1e1e),
  onPrimary = Color(0xFFe8f0ed),

  error = Color(0xFFe63739),

  )

@Composable
fun TrilhaVivaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  highContrast: Boolean = false,
  // Dynamic color is available on Android 12+
  fontSize: FontSize = FontSize.NORMAL,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {

  val colorScheme = when {
    darkTheme && !highContrast-> DarkColorScheme
    darkTheme && highContrast-> DarkHighContrastColorScheme
    !darkTheme && highContrast -> LightHighContrastColorScheme
    else -> LightColorScheme
  }




  val typography = when(fontSize){
    FontSize.SMALL -> TypographySmall
    FontSize.NORMAL -> TypographyNormal
    FontSize.BIG -> TypographyBig
    FontSize.EXTRA -> TypographyExtraBig
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = typography,
    content = content
  )
}