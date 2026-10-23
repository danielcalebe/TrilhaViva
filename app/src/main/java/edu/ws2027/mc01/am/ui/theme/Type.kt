package edu.ws2027.mc01.am.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.ws2027.mc01.am.R

// Set of Material typography styles to start with
val JetBrainsMono = FontFamily(
  Font(R.font.jetbrains_mono_regular, FontWeight.Normal)
)

val Nunito = FontFamily(
  Font(R.font.nunito_bold, FontWeight.Bold),
  Font(R.font.nunito_regular, FontWeight.Normal),
  Font(R.font.nunito_semibold, FontWeight.SemiBold),
)

val Typography = Typography(
  bodyLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  titleLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
  ),
  labelSmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 14.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.2.sp,
    fontWeight = FontWeight.Normal
  )
)