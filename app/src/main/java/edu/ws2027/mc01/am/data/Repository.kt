package edu.ws2027.mc01.am.data

import android.content.Context
import kotlinx.serialization.json.Json

class Repository(
  private val context: Context
) {

  fun getOnBoardingData(): List<OnBoardingItem> {
    return Json.decodeFromString<List<OnBoardingItem>>(
      context.assets.open("/data/onboarding.json").bufferedReader().readText()
    )
  }

  fun getProgressData(): ProgressData {
    return Json.decodeFromString(
      context.assets.open("data/progress.json").bufferedReader().readText()
    )
  }


}