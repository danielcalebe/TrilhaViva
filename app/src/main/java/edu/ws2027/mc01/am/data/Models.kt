package edu.ws2027.mc01.am.data

import androidx.annotation.DrawableRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class OnBoardingItem(
  @DrawableRes val image: Int,
  val title: String,
  val text: String,
)


@Serializable
data class Trail(
  @SerialName("id")
  val id: String,
  @SerialName("name_en")
  val nameEn: String,
  @SerialName("name_pt")
  val namePt: String,
  @SerialName("progress")
  val progress: Int
)

@Serializable
data class ProgressData(
  @SerialName("overall")
  val overall: Int,
  @SerialName("trails")
  val trails: List<Trail>
)


@Serializable
data class ScanResult(
  val format: String,
  val content: String,
  val type: Int, // should use stringResource()
)

@Serializable
data class Trail2(
  val id: String,

  @SerialName("name_en")
  val nameEn: String,

  @SerialName("name_pt")
  val namePt: String,


  @SerialName("image")
  val imageName: String,


  @SerialName("length_km")
  val length: Float,

  val difficulty: String
)

@Serializable
data class AudioGuide(
  @SerialName("duration_sec")
  val durationSec: Int,
  @SerialName("file")
  val `file`: String,
  @SerialName("id")
  val id: String,
  @SerialName("title_en")
  val titleEn: String,
  @SerialName("title_pt")
  val titlePt: String
)


enum class AudioPlayerPhase {
  IDLE, PLAYING, PAUSED
}

enum class VideoPlayerPhase {
  IDLE, PLAYING, PAUSED
}
data class AudioPlayerState(
  val phase: AudioPlayerPhase = AudioPlayerPhase.IDLE,
  val currentAudio: AudioGuide? = null,
  val durationMillis: Long = 0L,
  val progressMillis: Long = 0L,
)
@Serializable
data class Video(
    @SerialName("duration_sec")
    val durationSec: Int,
    @SerialName("file")
    val `file`: String,
    @SerialName("id")
    val id: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title_en")
    val titleEn: String,
    @SerialName("title_pt")
    val titlePt: String
)

@Serializable
data class SampleResponse(
    @SerialName("request")
    val request: Request,
    @SerialName("settings")
    val settings: Settings,
    @SerialName("stats")
    val stats: Stats,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("trails")
    val trails: List<TrailR>
)

@Serializable
data class Request(
    @SerialName("cached")
    val cached: Boolean,
    @SerialName("details")
    val details: Details,
    @SerialName("id")
    val id: String,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean,
    @SerialName("timestamp")
    val timestamp: Int
)

@Serializable
data class Settings(
    @SerialName("font_scale")
    val fontScale: Double,
    @SerialName("high_contrast")
    val highContrast: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("notifications")
    val notifications: Notifications,
    @SerialName("theme")
    val theme: String
)

@Serializable
data class Stats(
    @SerialName("average_length_km")
    val averageLengthKm: Double,
    @SerialName("cached_files")
    val cachedFiles: Int,
    @SerialName("completed")
    val completed: Int,
    @SerialName("completion_rate")
    val completionRate: Double,
    @SerialName("export_pending")
    val exportPending: Boolean,
    @SerialName("last_update")
    val lastUpdate: String?,
    @SerialName("sync_enabled")
    val syncEnabled: Boolean,
    @SerialName("total_trails")
    val totalTrails: Int
)

@Serializable
data class TrailR(
    @SerialName("description")
    val description: String,
    @SerialName("difficulty")
    val difficulty: String,
    @SerialName("elevation_m")
    val elevationM: Int,
    @SerialName("facilities")
    val facilities: Facilities,
    @SerialName("id")
    val id: String,
    @SerialName("length_km")
    val lengthKm: Double,
    @SerialName("name")
    val name: String,
    @SerialName("open")
    val `open`: Boolean,
    @SerialName("waypoints")
    val waypoints: List<Waypoint>
)

@Serializable
data class Details(
    @SerialName("attempts")
    val attempts: Int,
    @SerialName("elapsed_ms")
    val elapsedMs: Double,
    @SerialName("headers")
    val headers: Headers,
    @SerialName("retry")
    val retry: String?,
    @SerialName("source")
    val source: String
)

@Serializable
data class Headers(
    @SerialName("compression")
    val compression: Compression,
    @SerialName("content_type")
    val contentType: String,
    @SerialName("language")
    val language: String
)

@Serializable
data class Compression(
    @SerialName("algorithm")
    val algorithm: String,
    @SerialName("enabled")
    val enabled: Boolean,
    @SerialName("fallback")
    val fallback: Fallback,
    @SerialName("level")
    val level: Int,
    @SerialName("ratio")
    val ratio: Double
)

@Serializable
data class Fallback(
    @SerialName("reason")
    val reason: String?,
    @SerialName("used")
    val used: Boolean
)

@Serializable
data class Notifications(
  @SerialName("channels")
    val channels: List<String>,
  @SerialName("enabled")
    val enabled: Boolean,
  @SerialName("rules")
    val rules: Rules
)

@Serializable
data class Rules(
    @SerialName("max_per_day")
    val maxPerDay: Int,
    @SerialName("quiet_hours")
    val quietHours: QuietHours
)

@Serializable
data class QuietHours(
    @SerialName("end")
    val end: String,
    @SerialName("override")
    val `override`: Override,
    @SerialName("start")
    val start: String
)

@Serializable
class Override

@Serializable
data class Facilities(
    @SerialName("parking")
    val parking: Boolean,
    @SerialName("restrooms")
    val restrooms: Boolean?,
    @SerialName("water")
    val water: Boolean
)

@Serializable
data class Waypoint(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double,
    @SerialName("name")
    val name: String
)

enum class FontSize {
  SMALL, NORMAL, BIG, EXTRA
}