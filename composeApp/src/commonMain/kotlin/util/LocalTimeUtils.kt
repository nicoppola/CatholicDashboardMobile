package util

import data.HourId
import kotlinx.datetime.LocalTime

fun LocalTime.to24HourTime(): String {
    return this.hour.toString().padStart(2, '0') + ":" + this.minute.toString().padStart(2, '0')
}

fun LocalTime.toMeridianTime(): String {
    val hour12 = if (this.hour % 12 == 0) 12 else this.hour % 12
    val amPm = if (this.hour < 12) "am" else "pm"
    return hour12.toString() + ":" + this.minute.toString().padStart(2, '0') + amPm
}

fun HourId.contains(now: LocalTime): Boolean {
    return this.timeStart.isEarlierOrEqualTo(now) && this.timeEnd.isLaterOrEqualTo(now)
}

fun LocalTime.isEarlierOrEqualTo(time: LocalTime): Boolean {
    return this <= time
}

fun LocalTime.isLaterOrEqualTo(time: LocalTime): Boolean {
    return this >= time
}