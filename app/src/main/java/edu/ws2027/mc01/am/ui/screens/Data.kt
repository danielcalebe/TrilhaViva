package edu.ws2027.mc01.am.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.data.Repository
import org.json.JSONArray
import org.json.JSONObject

enum class JsonType {
  OBJECT, ARRAY, STRING, BOOLEAN, NUMBER, NULL
}


@Composable
fun Data(modifier: Modifier = Modifier, repository: Repository) {

  val treeString = stringResource(R.string.tree)
  val rawString = stringResource(R.string.raw)

  val sections = remember { listOf(treeString, rawString) }
  var section by remember { mutableStateOf(treeString) }

  val data by remember { mutableStateOf(repository.getSampleResponse()) }
  val dataString by remember { mutableStateOf(repository.getSampleResponseString()) }


  val stringColor = remember { Color(0xFF52C452) }
  val numberColor = remember { Color(0xFF20A7E8) }
  val booleanColor = remember { Color(0xFFF8653B) }
  val nullColor = remember { Color(0xFF848486) }
  val primaryColor = MaterialTheme.colorScheme.primary

  Column(
    modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = stringResource(R.string.data_viewer), style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )
    )


    ToggleViewer(
      sections = sections, section = section, onSectionChange = { section = it })





    when (section) {
      treeString -> {
        var isAllExpanded by remember { mutableStateOf(false) }
        var isAllCollapsed by remember { mutableStateOf(false) }

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .clickable {
                if (!isAllExpanded) {
                  isAllExpanded = true
                  isAllCollapsed = false
                }
              }
              .padding(vertical = 16.dp, horizontal = 1.dp)) {
            Text(
              text = stringResource(R.string.expand_all),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold
            )
          }


          Box(
            modifier = Modifier

              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .clickable {
                if (!isAllCollapsed) {
                  isAllCollapsed = true
                  isAllExpanded = false
                }
              }
              .padding(vertical = 16.dp, horizontal = 1.dp)) {
            Text(
              text = stringResource(R.string.collapse_all),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold
            )
          }
        }


        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
          val sampleObj = org.json.JSONObject(dataString)
          sampleObj.keys().forEach { key ->

            val item = sampleObj.get(key)
            ViewItem(item, key = key, true, isAllExpanded, isAllCollapsed)
          }
        }
      }


      rawString -> {
        Text(
          text = dataString,
          style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface),
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
        )


      }

    }
  }
}


@Composable
fun ViewItem(
  item: Any,
  key: String,
  isFirstLevel: Boolean = false,
  isAllExpanded: Boolean,
  isAllCollapsed: Boolean,
) {
  Row(
    modifier = Modifier.padding(start = 16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    val stringColor = remember { Color(0xFF52C452) }
    val numberColor = remember { Color(0xFF03A9F4) }
    val booleanColor = remember { Color(0xFFF8653B) }
    val nullColor = remember { Color(0xFF848486) }
    val primaryColor = MaterialTheme.colorScheme.primary

    when (item) {
      is JSONObject -> {
        var isExpanded by remember(isAllExpanded, isAllCollapsed) {
          mutableStateOf(
            if (isAllCollapsed) false
            else if (isAllExpanded) true
            else if (isFirstLevel) true
            else false
          )
        }
        if (isExpanded) {
          androidx.compose.material3.Icon(
            Icons.Default.KeyboardArrowDown,
            null,
            modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
          )
        } else {
          androidx.compose.material3.Icon(
            Icons.Default.KeyboardArrowRight,
            null,
            modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
          )
        }
        Column() {
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "\"$key\"",
              color = MaterialTheme.colorScheme.primary,
              style = MaterialTheme.typography.labelSmall
            )
            Text(
              text = ":", color = MaterialTheme.colorScheme.onSurface
            )
            if (!isExpanded) {

              Text(
                "{ ${item.length().toString()} }", style = MaterialTheme.typography.labelSmall
              )
            }
          }
          if (isExpanded) {
            item.keys().forEach { key ->
              Column() {
                ViewItem(
                  item.get(key), key, isAllExpanded = isAllExpanded, isAllCollapsed = isAllCollapsed
                )
              }
            }
          }

        }
      }

      is JSONArray -> {
        var isExpanded by remember(isAllExpanded, isAllCollapsed) {
          mutableStateOf(
            if (isAllCollapsed) false
            else if (isAllExpanded) true
            else if (isFirstLevel) true
            else false
          )
        }
        if (isExpanded) {
          androidx.compose.material3.Icon(
            Icons.Default.KeyboardArrowDown,
            null,
            modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
          )
        } else {
          androidx.compose.material3.Icon(
            Icons.Default.KeyboardArrowRight,
            null,
            modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
          )
        }
        Column() {
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(

              text = "\"$key\"",

              color = MaterialTheme.colorScheme.primary,

              style = MaterialTheme.typography.labelSmall
            )
            Text(
              text = ":", color = MaterialTheme.colorScheme.onSurface,

              style = MaterialTheme.typography.labelSmall
            )
            if (!isExpanded) {
              Text(
                "[ ${item.length().toString()} ]",

                style = MaterialTheme.typography.labelSmall
              )
            }
          }
          if (isExpanded) {
            for (i in 0 until item.length()) {
              Column() {
                ViewItem(
                  item.get(i), key, isAllExpanded = isAllExpanded, isAllCollapsed = isAllCollapsed
                )
              }
            }
          }

        }
      }


      is String -> {
        Text(
          text = "\"$key\"", color = MaterialTheme.colorScheme.primary,

          style = MaterialTheme.typography.labelSmall
        )
        Text(
          text = ":",
          color = MaterialTheme.colorScheme.onSurface,
          style = MaterialTheme.typography.labelSmall
        )

        Text(
          "\"$item\"", color = stringColor,

          style = MaterialTheme.typography.labelSmall
        )
      }

      is Boolean -> {
        Text(
          text = "\"$key\"", color = MaterialTheme.colorScheme.primary,

          style = MaterialTheme.typography.labelSmall
        )
        Text(
          text = ":", color = MaterialTheme.colorScheme.onSurface,

          style = MaterialTheme.typography.labelSmall
        )

        Text(
          item.toString(), color = booleanColor,

          style = MaterialTheme.typography.labelSmall
        )

      }

      else -> {
        if (item.toString() != "null") {
          Text(
            text = "\"$key\"", color = MaterialTheme.colorScheme.primary,

            style = MaterialTheme.typography.labelSmall
          )
          Text(
            text = ":", color = MaterialTheme.colorScheme.onSurface,

            style = MaterialTheme.typography.labelSmall
          )

          Text(
            item.toString(), color = numberColor,

            style = MaterialTheme.typography.labelSmall
          )
        } else {
          Text(
            text = "\"$key\"", color = MaterialTheme.colorScheme.primary,

            style = MaterialTheme.typography.labelSmall
          )
          Text(
            text = ":", color = MaterialTheme.colorScheme.onSurface,

            style = MaterialTheme.typography.labelSmall
          )

          Text(
            item.toString(), color = nullColor,

            style = MaterialTheme.typography.labelSmall
          )
        }
      }

    }
  }
}


@Composable
fun ToggleViewer(
  modifier: Modifier = Modifier,
  sections: List<String>,
  section: String,
  onSectionChange: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(4.dp)
  ) {
    sections.forEach {
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(12.dp))
          .background(
            if (section == it) MaterialTheme.colorScheme.primary
            else Color.Transparent
          )
          .clickable(onClick = { onSectionChange(it) })
          .padding(12.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = it, style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = if (section == it) FontWeight.Bold else FontWeight.Normal,
            color = if (section == it) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
          )
        )
      }
    }
  }
}