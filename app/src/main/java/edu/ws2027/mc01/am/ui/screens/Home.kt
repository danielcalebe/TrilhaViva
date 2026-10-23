package edu.ws2027.mc01.am.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.data.ProgressData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Home(modifier: Modifier = Modifier, progressData: ProgressData) {

  val scope = rememberCoroutineScope()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurface
        )
      )
      Text(
        text = stringResource(R.string.your_trail_companion),
        style = MaterialTheme.typography.bodyLarge.copy(
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.SemiBold,
          fontSize = 16.sp
        ),
        color = MaterialTheme.colorScheme.onSurface
      )

    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    HomeCard(
      modifier = Modifier,
      title = stringResource(R.string.overall_progress)
    ) {

      val progress = remember { Animatable(0f) }
      LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(1200, easing = LinearEasing))
      }



      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {

        Box(
          modifier = Modifier
            .size(110.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(
            modifier = Modifier
              .fillMaxSize()
              .clickable(
                onClick = {
                  scope.launch {
                    progress.snapTo(0f)
                    progress.animateTo(1f, tween(1200, easing = LinearEasing))
                  }
                },
                indication = null,
                interactionSource = null
              )
          ) {
            drawArc(
              color = surfaceColor,
              startAngle = 0f,
              sweepAngle = 360f,
              useCenter = false,
              style = Stroke(36f)
            )

            drawArc(
              color = primaryColor,
              startAngle = 270f,
              sweepAngle = 360f * (progress.value * progressData.overall / 100),
              useCenter = false,
              style = Stroke(36f, cap = StrokeCap.Round)
            )

          }
          Text(
            text = "${(progress.value * progressData.overall).toInt()}%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              lineHeight = 24.sp,
            ),
          )
        }

        val completedTrails = progressData.trails.count { it.progress == 100 }
        if (completedTrails > 0) {
          Text(
            text = pluralStringResource(
              R.plurals.completed_trails,
              completedTrails,
              completedTrails
            ),
//          text = stringResource(R.string.completed_trails_one),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
          )
        }


      }
    }

    HomeCard(
      modifier = Modifier,
      title = stringResource(R.string.trail_progress)
    ) {
      progressData.trails.forEachIndexed { index, trail ->
        val progress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
          val delay = (index + 1) * 150
          delay(delay.milliseconds)
          progress.animateTo(
            targetValue = 1f,
            tween(1000, easing = LinearEasing)
          )
        }
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
          verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = if (Locale.getDefault().toString() == "pt_BR") trail.namePt else trail.nameEn,
              style = MaterialTheme.typography.bodyLarge
                .copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${(trail.progress * progress.value).toInt()}%",
              style = MaterialTheme.typography.bodyLarge
                .copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )

          }

          LinearProgressIndicator(
            progress = { trail.progress / 100f * progress.value },
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp),
            gapSize = -(12).dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface,
            drawStopIndicator = {}
          )
        }
      }
    }


    HomeCard(
      modifier = Modifier,
      title = stringResource(R.string.offline_pack)
    ) {
      val size = 12
      val progress = remember { Animatable(0f) }
      var isDownloading by remember { mutableStateOf(false) }
      var isDownloaded by remember { mutableStateOf(false) }


      suspend fun downloadFake() {
        if (isDownloading) {
          progress.snapTo(0f)
          isDownloading = false
        } else {
          if (!isDownloaded) {
            isDownloading = true
            progress.animateTo(
              targetValue = 1f,
              tween(5000, easing = LinearEasing)
            )
            isDownloaded = true
            isDownloading = false
          } else {
            progress.snapTo(0f)
            isDownloading = false
            isDownloading = true
            progress.animateTo(
              targetValue = 1f,
              tween(5000, easing = LinearEasing)
            )
            isDownloaded = true
            isDownloading = false
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        LinearProgressIndicator(
          progress = { progress.value },
          modifier = Modifier
            .fillMaxWidth()
            .height(12.dp),
          gapSize = -(12).dp,
          color = MaterialTheme.colorScheme.primary,
          trackColor = MaterialTheme.colorScheme.surface,
          drawStopIndicator = {}
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${(progress.value * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge
              .copy(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "${
              "%.1f".format(
                locale = Locale.US,
                (size * progress.value).toFloat()
              )
            }/${"%.1f".format(locale = Locale.US, size.toFloat())}",

            style = MaterialTheme.typography.bodyLarge
              .copy(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Spacer(Modifier.height(8.dp))

        Box(
          modifier = Modifier

            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {


              scope.launch {
                downloadFake()
              }

//              if (isDownloading) {
//                isDownloading = false
//              } else {
//                isDownloading = true
//              }

            }
            .padding(vertical = 8.dp, horizontal = 1.dp)
        ) {
          val btnText =
            if (isDownloading) stringResource(R.string.cancel)
            else {
              if (isDownloaded) stringResource(R.string.download_again)
              else stringResource(R.string.start_download)
            }
          Text(
            text = btnText,
            color = MaterialTheme.colorScheme.surface,
            fontWeight = FontWeight.SemiBold
          )
        }


      }
    }
  }
}

@Composable
fun HomeCard(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(12))
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(vertical = 16.dp, horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodyLarge.copy(
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
      ),
      color = MaterialTheme.colorScheme.onSurface
    )
    content()

  }
}


//}
//
//@Preview
//@Composable
//private fun Preview_Home() {
//  TrilhaVivaTheme() {
//
//    Home()
//  }
//}