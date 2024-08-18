package networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NovusResponse(
    val date: String?,
    val season: String?,
//    @SerialName("seasonWeek")
    val season_week: Int?,
    val weekday: String?,
    val celebrations: List<celebration>?,
)

@Serializable
data class celebration(
    val title: String?,
    val colour: String?,
    val rank: String?,
  //  @SerialName("rankNum")
    val rank_num: Double?,
)