package edu.ws2027.mc01.am

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.ws2027.mc01.am.data.Repository
import edu.ws2027.mc01.am.ui.screens.Home
import edu.ws2027.mc01.am.ui.screens.OnBoarding
import edu.ws2027.mc01.am.ui.screens.Pin
import edu.ws2027.mc01.am.ui.screens.Scan
import edu.ws2027.mc01.am.ui.screens.Splash
import edu.ws2027.mc01.am.ui.theme.TrilhaVivaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {

      val repository = remember { Repository(applicationContext) }

      val nav = rememberNavController()
      val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route
      TrilhaVivaTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize(), bottomBar = {
            if (currentRoute in listOf("home", "scan", "media", "data", "settings")) {
              NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
              ) {
                val selected = MaterialTheme.colorScheme.primary
                val unselected = MaterialTheme.colorScheme.onSurfaceVariant
                val indicator = MaterialTheme.colorScheme.surface

                NavigationBarItem(
                  selected = currentRoute == "home",
                  onClick = { nav.navigate("home") },
                  icon = {
                    Icon(
                      Icons.Outlined.Home,
                      null,
                    )
                  },
                  label = {
                    Text(
                      stringResource(R.string.home),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,

                        ),
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicator,
                    selectedIconColor = selected,
                    unselectedIconColor = unselected,
                    selectedTextColor = selected,
                    unselectedTextColor = unselected
                  )
                ) // Home
                NavigationBarItem(
                  selected = currentRoute == "scan",
                  onClick = { nav.navigate("scan") },
                  icon = {
                    Icon(
                      Icons.Outlined.QrCodeScanner,
                      null,
                    )
                  },
                  label = {
                    Text(
                      stringResource(R.string.scan),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                      ),
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicator,
                    selectedIconColor = selected,
                    unselectedIconColor = unselected,
                    selectedTextColor = selected,
                    unselectedTextColor = unselected
                  )
                )
                NavigationBarItem(
                  selected = currentRoute == "media",
                  onClick = { nav.navigate("media") },
                  icon = {
                    Icon(
                      Icons.Outlined.PlayCircle,
                      null,
                    )
                  },
                  label = {
                    Text(
                      stringResource(R.string.media),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,

                        ),
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicator,
                    selectedIconColor = selected,
                    unselectedIconColor = unselected,
                    selectedTextColor = selected,
                    unselectedTextColor = unselected
                  )
                )
                NavigationBarItem(
                  selected = currentRoute == "data",
                  onClick = { nav.navigate("data") },
                  icon = {
                    Icon(
                      Icons.Outlined.Dataset,
                      null,
                    )
                  },
                  label = {
                    Text(
                      stringResource(R.string.data),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,

                        ),
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicator,
                    selectedIconColor = selected,
                    unselectedIconColor = unselected,
                    selectedTextColor = selected,
                    unselectedTextColor = unselected
                  )
                )
                NavigationBarItem(
                  selected = currentRoute == "settings",
                  onClick = { nav.navigate("settings") },
                  icon = {
                    Icon(
                      Icons.Default.Tune,
                      null,
                    )
                  },
                  label = {
                    Text(
                      stringResource(R.string.settings),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,

                        ),
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicator,
                    selectedIconColor = selected,
                    unselectedIconColor = unselected,
                    selectedTextColor = selected,
                    unselectedTextColor = unselected
                  )
                )
              }
            }
          }) { innerPadding ->
          Column(
            modifier = Modifier.padding(innerPadding)
          ) {
            NavHost(nav, "splash") {
              composable(
                route = "splash",
                exitTransition = {
                  fadeOut(
                    animationSpec = tween(
                      300,
                      0,
                      LinearEasing
                    )
                  )
                }) { Splash() { nav.navigate("on-boarding") } }

              composable(
                route = "on-boarding",
                enterTransition = { fadeIn(animationSpec = tween(300, 0, LinearEasing)) }) {
                OnBoarding(
                  onNavToPin = { nav.navigate("pin") })
              }

              composable("pin") {
                Pin() { nav.navigate("home") }
              }

              composable("home") {
                Home(progressData = repository.getProgressData())
              }
              composable("scan") { Scan() }

            }

          }
        }
      }
    }
  }
}