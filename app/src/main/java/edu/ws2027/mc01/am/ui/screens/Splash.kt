package edu.ws2027.mc01.am.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.ui.theme.TrilhaVivaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Splash(modifier: Modifier = Modifier, onNavigate: () -> Unit) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary),
  ) {

    var isSunVisible by remember { mutableStateOf(false) }
    var isMountainVisible by remember { mutableStateOf(false) }
    var isLogoVisible by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
      isSunVisible = true
      delay(800.milliseconds)
      isMountainVisible = true
      delay(800.milliseconds)
      isLogoVisible = true
      isTextVisible = true
      delay(1200.milliseconds)
      onNavigate()
    }


    AnimatedVisibility(
      visible = isSunVisible,
      label = "sun",
      enter =
        slideInVertically(
          initialOffsetY = { it },
          animationSpec = tween(400)
        ) + scaleIn(
          animationSpec = tween(800, easing = EaseOut),
          initialScale = 0.6f,
        ),
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
        .padding(top = 300.dp)
    ) {
      Image(
        painter = painterResource(R.drawable.sun),
        contentDescription = "Sun",
        modifier = Modifier.size(100.dp)
      )
    }


    AnimatedVisibility(
      visible = isMountainVisible,
      label = "mountain",
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 320.dp),
      enter = expandHorizontally(
        initialWidth = { -(it - 200) },
        animationSpec = tween(800, easing = LinearEasing),
        expandFrom = Alignment.Start,
      )
    ) {
      Image(
        painter = painterResource(R.drawable.mountain),
        contentDescription = "Mountain",
        contentScale = ContentScale.FillWidth,
      )
    }

    AnimatedVisibility(
      visible = isLogoVisible,
      label = "leaf",
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 380.dp),
      enter = scaleIn(initialScale = 0f, animationSpec = tween(600)) + scaleIn(spring())
    ) {
      Image(
        painter = painterResource(R.drawable.leaf),
        contentDescription = "Laef",
        contentScale = ContentScale.FillWidth,
      )
    }

    AnimatedVisibility(
      visible = isTextVisible,
      label = "text",
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Center)
        .padding(top = 20.dp),

      ) {
      TextAnimationMotion("TrilhaViva")
    }

  }
}

@Composable
private fun TextAnimationMotion(text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    text.forEachIndexed { index, char ->
      var isVisible by remember { mutableStateOf(false) }
      LaunchedEffect(Unit) {
        isVisible = true
      }


      AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
          initialOffsetY = { it + 2 },
          animationSpec = tween(400, delayMillis = (index * 50))
        )
      ) {
        Text(
          text = char.toString(),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 32.sp
          ),
          textAlign = TextAlign.Center
        )
      }
    }

  }
}

@Preview
@Composable
private fun Preview_Splash() {
  TrilhaVivaTheme(false) {
    Splash(onNavigate = {})
  }
}