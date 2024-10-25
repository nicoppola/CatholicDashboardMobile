package domain

import data.CalendarData
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.main.ListObject

class GetOfficeListItemUseCase {
    operator fun invoke(office: CalendarData.Office): List<ListObject> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val hours = LiturgyHours.contains(now.hour)
        return hours.map {
            ListObject(
                title = "Divine Office",
                text = it.label + "Prayer",
                link = getLink(office, it)
            )
        }
    }
}

private fun Int.between(a: Double, b: Double): Boolean {
    return a > this && this < b
}

private fun getLink(data: CalendarData.Office, hour: LiturgyHours): String {
    return when (hour) {
        LiturgyHours.LAUDS -> data.morning
        LiturgyHours.TERCE -> data.midMorning
        LiturgyHours.SEXT -> data.midday
        LiturgyHours.NONE -> data.midAfternoon
        LiturgyHours.VESPERS -> data.evening
        LiturgyHours.COMPLINE -> data.night
    }
}

//TODO get these hours and ranges from system preferences
private enum class LiturgyHours(val hour: Int, val label: String) {
    LAUDS(6, "Morning"),
    TERCE(9, "Mid-morning"),
    SEXT(12, "Midday"),
    NONE(15, "Mid-afternoon"),
    VESPERS(18, "Evening"),
    COMPLINE(21, "Night");

    companion object {
        fun contains(currHour: Int): List<LiturgyHours> {
            val list = mutableListOf<LiturgyHours>()
             entries.toTypedArray().forEach {
                val early = if (it == LAUDS) {
                    0.0
                } else {
                    it.hour - 1.5
                }
                val late = if (it == COMPLINE) {
                    24.0
                } else {
                    it.hour + 1.5
                }
                if(currHour.between(early, late)){
                    list.add(it)
                }
            }
            return list.toList()
        }
    }
}