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

  fun getTrails(): List<Trail2> {
    return Json.decodeFromString(
      context.assets.open("data/trails.json").bufferedReader().readText()
    )
  }

  fun getAudioGuides(): List<AudioGuide> {
    return Json.decodeFromString(
      context.assets.open("data/audios.json").bufferedReader().readText()
    )
  }

  fun getVideos(): List<Video> {
    return Json.decodeFromString(
      context.assets.open("data/videos.json").bufferedReader().readText()
    )
  }

  fun getSampleResponse(): SampleResponse {
    return Json.decodeFromString(
      context.assets.open("data/sample_response.json").bufferedReader().readText()
    )
  }

  fun getSampleResponseString(): String {
    return context.assets.open("data/sample_response.json").bufferedReader().readText()
  }


}