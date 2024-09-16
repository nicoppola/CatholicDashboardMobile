package networking

import kotlinx.serialization.Serializable

@Serializable
data class DailyDataResponse(
    val date: String,
    val season: String,
    val color: String,
)
