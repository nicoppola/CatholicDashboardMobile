package data

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class HourId(
    val novusLabel: String,
    val latinLabel: String,
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)