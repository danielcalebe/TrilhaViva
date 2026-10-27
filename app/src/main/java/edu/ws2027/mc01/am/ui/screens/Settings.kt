package edu.ws2027.mc01.am.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.data.FontSize
import java.util.Locale

@SuppressLint("NonObservableLocale")
@Composable
fun Settings(
  modifier: Modifier = Modifier,
  isDarkMode: Boolean,
  onChangeDarkMode: (Boolean) -> Unit,
  currentFontSize: Int,
  onChangeFontSize: (FontSize) -> Unit,
  isHighContrast: Boolean,
  onChangeHighContrast: (Boolean) -> Unit
) {

  Column(
    modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = stringResource(R.string.settings), style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )
    )

    SettingsCard(title = stringResource(R.string.appearance)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(stringResource(R.string.dark_mode))
        Switch(
          checked = isDarkMode,
          onCheckedChange = { onChangeDarkMode(!isDarkMode) },
          colors = SwitchDefaults.colors(
            uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
            checkedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.surface,
            checkedBorderColor = Color.Transparent,
            uncheckedBorderColor = Color.Transparent,
          )
        )
      }
    }

    SettingsCard(
      title = stringResource(R.string.accessibility)
    ) {
      Column() {
        Text(stringResource(R.string.font_size))
        Spacer(Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FontSize.values().forEachIndexed { index, fs ->
            val n = index + 1
            val string = when (n) {
              1 -> stringResource(R.string.small)
              3 -> stringResource(R.string.large)
              4 -> stringResource(R.string.extra_large)
              else -> stringResource(R.string.normal)
            }
            val selected = currentFontSize == n

            Text(
              text = string,
              style = MaterialTheme.typography.bodyLarge,
              modifier = Modifier
                .border(
                  1.dp,
                  shape = CircleShape,
                  color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                .clip(CircleShape)
                .background(
                  if (selected) MaterialTheme.colorScheme.surface
                  else Color.Transparent
                )
                .clickable(onClick = {
                  onChangeFontSize(fs)
                })
                .padding(vertical = 4.dp, horizontal = 6.dp),
              color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
          }
        }


        Spacer(Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(stringResource(R.string.high_contrast))
          Switch(
            checked = isHighContrast,
            onCheckedChange = { onChangeHighContrast(!isHighContrast) },
            colors = SwitchDefaults.colors(
              uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
              checkedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
              checkedTrackColor = MaterialTheme.colorScheme.primary,
              uncheckedTrackColor = MaterialTheme.colorScheme.surface,
              checkedBorderColor = Color.Transparent,
              uncheckedBorderColor = Color.Transparent,
            )
          )
        }

        Text(
          text = stringResource(R.string.the_quick_brown_fox_jumps_over_the_lazy_dog),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              MaterialTheme.colorScheme.surface

            )
            .padding(8.dp)
        )


      }
    }


    val context = LocalContext.current
    SettingsCard(
      title = stringResource(R.string.storage)
    ) {
      var files by remember { mutableStateOf(context.cacheDir.listFiles()) }
      var totalSize by remember(files) {
        mutableStateOf(files.filter {
          it.name.contains("IMAGE_") || it.name.contains("Gravação")
        }.sumOf {
          it.length().toDouble() / 100000
        })
      }

      Column() {
        Text(
          if (totalSize.toInt() == 0) {
            "${stringResource(R.string.temporary_photos)}: 0 KB"
          } else {
            "${stringResource(R.string.temporary_photos)}: ${
              "%.2f".format(
                totalSize
              )
            } MB"
          }
        )
        var showClearCacheDialog by remember { mutableStateOf(false) }
        if (showClearCacheDialog) {
          AlertDialog(onDismissRequest = { showClearCacheDialog = false }, title = {
            Text(stringResource(R.string.clear_cache))
          }, text = {
            Text(stringResource(R.string.clear_all_temporary_files))
          }, confirmButton = {
            Button(onClick = {
              context.cacheDir.listFiles { it.name.contains("IMAGE_") }.forEach {
                it.delete()
              }
              context.cacheDir.listFiles { it.name.contains("Gravação") }.forEach {
                it.delete()
              }
              files = context.cacheDir.listFiles()
              showClearCacheDialog = false
            }) {
              Text(stringResource(R.string.yes), style = MaterialTheme.typography.bodyLarge)
            }
          }, dismissButton = {
            OutlinedButton(
              onClick = { showClearCacheDialog = false }) {
              Text(stringResource(R.string.no), style = MaterialTheme.typography.bodyLarge)
            }
          })
        }
        Box(
          modifier = Modifier

            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
              showClearCacheDialog = true
            }
            .padding(vertical = 16.dp, horizontal = 1.dp)) {
          Text(
            text = stringResource(R.string.clear_cache),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }



    SettingsCard(
      title = stringResource(R.string.storage)
    ) {
      Column() {
        Text(stringResource(R.string.the_language_follows_the_device_settings))

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 16.dp, horizontal = 1.dp)) {
          Text(
            text = stringResource(
              if(Locale.getDefault().language == "pt-BR" ) R.string.portugu_s_brasil
              else R.string.english

            ),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }


}

@Composable
fun SettingsCard(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
  Column(
    modifier = modifier
      .dropShadow(
        shape = RoundedCornerShape(12.dp),
        shadow = Shadow(
          radius = 2.dp,
          alpha = 0.1f,
          spread = 1.dp,
          blendMode = BlendMode.Darken
        )
      )
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    Text(
      title,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
      )
    )
    content()
  }

}