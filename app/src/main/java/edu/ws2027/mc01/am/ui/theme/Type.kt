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

val TypographyNormal = Typography(
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

val TypographySmall = Typography(
  bodyLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp * 0.85f,
    lineHeight = 16.sp * 0.85f,
    letterSpacing = 0.5.sp *0.85f
  ),
  titleLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp   *0.85f,
    lineHeight = 28.sp   *0.85f,
    letterSpacing = 0.sp  *0.85f
  ),
  labelSmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp   *0.85f,
    lineHeight = 16.sp   *0.85f,
    letterSpacing = 0.5.sp  *0.85f
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 14.sp   *0.85f,
    lineHeight = 14.sp   *0.85f,
    letterSpacing = 0.2.sp   *0.85f,
    fontWeight = FontWeight.Normal
  )
)

val TypographyBig = Typography(
  bodyLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp   *1.3f,
    lineHeight = 16.sp *1.3f,
    letterSpacing = 0.5.sp *1.3f
  ),
  titleLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp  *1.3f,
    lineHeight = 28.sp  *1.3f,
    letterSpacing = 0.sp*1.3f
  ),
  labelSmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp *1.3f ,
    lineHeight = 16.sp  *1.3f,
    letterSpacing = 0.5.sp*1.3f
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 14.sp *1.3f,
    lineHeight = 14.sp *1.3f,
    letterSpacing = 0.2.sp *1.3f,
    fontWeight = FontWeight.Normal
  )
)
val TypographyExtraBig = Typography(
  bodyLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp *1.6f,
    lineHeight = 16.sp *1.6f,
    letterSpacing = 0.5.sp *1.6f
  ),
  titleLarge = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp *1.6f,
    lineHeight = 28.sp *1.6f,
    letterSpacing = 0.sp *1.6f
  ),
  labelSmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp *1.6f,
    lineHeight = 16.sp *1.6f,
    letterSpacing = 0.5.sp*1.6f
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 14.sp *1.6f,
    lineHeight = 14.sp *1.6f,
    letterSpacing = 0.2.sp *1.6f,
    fontWeight = FontWeight.Normal
  )
)