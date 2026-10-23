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


