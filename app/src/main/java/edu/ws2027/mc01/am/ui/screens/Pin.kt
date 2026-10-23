package edu.ws2027.mc01.am.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.ui.theme.TrilhaVivaTheme
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Pin(modifier: Modifier = Modifier, onNavToHome: () -> Unit) {
  var pin by remember { mutableStateOf("") }
  var animate by remember { mutableStateOf(true) }
  var isError by remember { mutableStateOf(false) }
  LaunchedEffect(pin) {
    if (pin.length == 4) {
      if (pin != "2027") {
        isError = true
        delay(300.milliseconds)
        pin = ""
        isError = false
      } else {
       onNavToHome()
      }
    }
  }

  fun writePin(char: Char) {

    if (char == '<') {
      if (pin.isNotEmpty()) {
        animate = false
        pin = pin.take(pin.length - 1)

      }
    } else {
      isError = false
      animate = true
      if (pin.length < 4) {
        pin += char
      }
    }
  }
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 64.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.enter_your_pin),
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurface
        )
      )
      Text(
        text = stringResource(R.string.use_the_4_digit_pin_to_unlock),
        style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }


    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
      ) {
        repeat(4) {


          val scale = remember { Animatable(1f) }

          LaunchedEffect(pin) {
            withContext(NonCancellable) {
              if (animate) {
                if (pin.length == it + 1) {
                  scale.animateTo(
                    targetValue = 1.3f,
                    animationSpec = spring(
                      stiffness = Spring.StiffnessHigh,
                      dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                  )
                  scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                      stiffness = Spring.StiffnessHigh,
                      dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                  )
                }
              }
            }
          }

          Box(
            modifier = Modifier
              .scale((scale.value))
              .size(16.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary.copy(if (pin.length >= it + 1) 1f else 0.5f))

          )
        }
      }

      Box(
        modifier = Modifier.height(24.dp)
      ) {
        if (isError) {
          Text(
            text = stringResource(R.string.incorrect_pin_try_again),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.error
          )
        }
      }
    }





    Column(
      modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(0.7f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
      ) {
        KeyboardDigit(modifier = Modifier, char = '1', onClick = { writePin('1') })
        KeyboardDigit(modifier = Modifier, char = '2', onClick = { writePin('2') })
        KeyboardDigit(modifier = Modifier, char = '3', onClick = { writePin('3') })
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
      ) {
        KeyboardDigit(modifier = Modifier, char = '4', onClick = { writePin('4') })
        KeyboardDigit(modifier = Modifier, char = '5', onClick = { writePin('5') })
        KeyboardDigit(modifier = Modifier, char = '6', onClick = { writePin('6') })
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
      ) {
        KeyboardDigit(modifier = Modifier, char = '7', onClick = { writePin('7') })
        KeyboardDigit(modifier = Modifier, char = '8', onClick = { writePin('8') })
        KeyboardDigit(modifier = Modifier, char = '9', onClick = { writePin('9') })
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
      ) {
        Box(modifier = Modifier.size(64.dp))
        KeyboardDigit(modifier = Modifier, char = '0', onClick = { writePin('0') })
        KeyboardDigit(modifier = Modifier, char = '<', onClick = { writePin('<') })
      }
    }
  }
}

@Composable
fun KeyboardDigit(modifier: Modifier = Modifier, char: Char, onClick: () -> Unit = { }) {
  val density = LocalDensity.current
  val textSize = 22.sp
  Box(
    modifier = modifier
      .dropShadow(
        shape = CircleShape,
        shadow = Shadow(
          radius = 4.dp,
          alpha = 0.2f,
          spread = 0.5.dp,
          color = MaterialTheme.colorScheme.onSurface,
          blendMode = BlendMode.Darken
        )
      )
      .clip(CircleShape)
      .size(64.dp)
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    when (char) {
      '<' -> {
        Icon(
          Icons.Default.Backspace,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(with(density) { textSize.toDp() })
        )
      }

      else -> {
        Text(
          text = char.toString(),
          color = MaterialTheme.colorScheme.onSurface,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = textSize,
            lineHeight = textSize,
          ),
        )
      }
    }
  }
}


@Preview
@Composable
private fun PreviewPin() {
  TrilhaVivaTheme() {
    Pin() {}
  }
}