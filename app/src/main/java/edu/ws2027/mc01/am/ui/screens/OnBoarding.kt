package edu.ws2027.mc01.am.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.ui.theme.TrilhaVivaTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class CarouselItem(
  @DrawableRes val image: Int,
  val title: String,
  val text: String,
)

@Composable
fun OnBoarding(modifier: Modifier = Modifier, onNavToPin: () -> Unit) {

  val context = LocalContext.current
  val images =
    remember { listOf(R.drawable.onboarding_1, R.drawable.onboarding_2, R.drawable.onboarding_3) }

  val title1 = stringResource(R.string.on_boarding_1_title)
  val title2 = stringResource(R.string.on_boarding_2_title)
  val title3 = stringResource(R.string.on_boarding_3_title)
  val description1 = stringResource(R.string.on_boarding_1_description)
  val description2 = stringResource(R.string.on_boarding_2_description)
  val description3 = stringResource(R.string.on_boarding_3_description)

  val carouselItems = remember {
    listOf(
      CarouselItem(
        image = images[0],
        title = title1,
        text = description1
      ),
      CarouselItem(
        image = images[1],
        title = title2,
        text = description2,
      ),
      CarouselItem(
        image = images[2],
        title = title3,
        text = description3
      )
    )
  }


  val pagerState = rememberPagerState(pageCount = { carouselItems.size })
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(top = 36.dp, end = 24.dp),
      horizontalArrangement = Arrangement.End
    ) {

      AnimatedVisibility(pagerState.pageCount - 1 != pagerState.currentPage) {
        TextButton(
          onClick = {
            onNavToPin()
          }
        ) {
          Text(
            text = stringResource(R.string.skip),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 50.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        HorizontalPager(
          state = pagerState,
          modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
          contentPadding = PaddingValues(),
          pageSpacing = -280.dp,

          ) { page ->
          Image(
            painter = painterResource(carouselItems.get(page).image),
            contentDescription = null,

            modifier = Modifier
//              .scale(1.4f)
              .fillMaxWidth()
              .graphicsLayer {
                val focus = 1f - pagerState.offsetForPage(page).absoluteValue.coerceIn(0f, 1f)
                val s = 0.85f + (focus * 0.15f) + 0.5f
                val a = 0.5f + (focus * 0.5f)
                scaleX = s
                scaleY = s
                alpha = a
              },
          )
        }


        val item = carouselItems.get(pagerState.currentPage)

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),

          verticalArrangement = Arrangement.spacedBy(8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
          Text(
            text = item.text,
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(Modifier.height(24.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
          repeat(pagerState.pageCount) {
            val updateTransition = updateTransition(pagerState)
            val width = updateTransition.animateDp() { state ->
              if (state.currentPage == it) 24.dp
              else 12.dp
            }
            val alpha = updateTransition.animateFloat() { state ->
              if (state.currentPage == it) 1f
              else 0.5f
            }

            Box(
              modifier = Modifier
                .clip(CircleShape)
                .height(12.dp)
                .width(width.value)
                .background(MaterialTheme.colorScheme.primary.copy(alpha.value))
            )
          }
        }

        Spacer(Modifier.height(16.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
        ) {

          val scope = rememberCoroutineScope()

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.primary)
              .clickable {
                if (pagerState.pageCount - 1 == pagerState.currentPage) {
                  onNavToPin()
                } else {
                  scope.launch {
                    pagerState.animateScrollToPage(
                      pagerState.currentPage + 1, 0f, tween(
                        500, 0,
                        LinearEasing
                      )
                    )
                  }
                }
              }
              .padding(vertical = 8.dp)
          ) {
            Text(
              text = stringResource(if (pagerState.pageCount - 1 == pagerState.currentPage) R.string.get_started else R.string.next),
              color = MaterialTheme.colorScheme.surface,
              fontWeight = FontWeight.SemiBold
            )
          }

        }


      }

    }
  }
}

fun PagerState.offsetForPage(page: Int): Float = (currentPage - page) + currentPageOffsetFraction

@Preview
@Composable
private fun Preview_OnBoarding() {
  TrilhaVivaTheme() {
    OnBoarding() {}
  }
}
