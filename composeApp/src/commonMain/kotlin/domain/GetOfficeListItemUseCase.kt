package domain

import data.CalendarData
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.coppola.catholic.OfficeSettings
import org.coppola.catholic.TimeSettings
import org.koin.compose.koinInject
import ui.main.ListObject

class GetOfficeListItemUseCase(
    private val preferences: PreferencesRepository
) {
    suspend operator fun invoke(office: CalendarData.Office): List<ListObject> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val prefs = preferences.getOffice().first()

        var hours = emptyList<ListObject>()
        if (prefs.enabled) {
            hours = LiturgyHours.entries.mapNotNull {
                it.contains(it.getTimeSetting(prefs), now)
            }.map {
                ListObject(
                    title = "Divine Office",
                    text = it.getText(it.getTimeSetting(prefs)),
                    link = it.getLink(office)
                )
            }
        }
        return hours
    }
}

private fun LiturgyHours.contains(prefs: TimeSettings?, now: LocalDateTime): LiturgyHours? {
    return if (prefs?.contains(now.hour, now.minute) == true) {
        this
    } else {
        null
    }
}

//todo put this in a use case or something; figure out time somewhere
private fun LiturgyHours.getText(prefs: TimeSettings?): String {
    val startTimeDisplay = prefs?.startTime?.hour.toString().padStart(2, '0') +
            prefs?.startTime?.minute.toString().padStart(2, '0')

    val endTimeDisplay = prefs?.endTime?.hour.toString().padStart(2, '0') +
            prefs?.endTime?.minute.toString().padStart(2, '0')

    return "${this.novusLabel}, $startTimeDisplay - $endTimeDisplay"
}

private fun LiturgyHours.getTimeSetting(prefs: OfficeSettings): TimeSettings? {
    return when (this) {
        LiturgyHours.LAUDS -> prefs.lauds
        LiturgyHours.TERCE -> prefs.terce
        LiturgyHours.SEXT -> prefs.sext
        LiturgyHours.NONE -> prefs.none
        LiturgyHours.VESPERS -> prefs.vespers
        LiturgyHours.COMPLINE -> prefs.compline
    }
}

private fun LiturgyHours.getLink(data: CalendarData.Office): String {
    return when (this) {
        LiturgyHours.LAUDS -> data.morning
        LiturgyHours.TERCE -> data.midMorning
        LiturgyHours.SEXT -> data.midday
        LiturgyHours.NONE -> data.midAfternoon
        LiturgyHours.VESPERS -> data.evening
        LiturgyHours.COMPLINE -> data.night
    }
}

private enum class LiturgyHours(val novusLabel: String, val latinLabel: String) {
    LAUDS("Morning", "Lauds"),
    TERCE("Mid-morning", "Terce"),
    SEXT("Midday", "Sext"), //
    NONE("Mid-afternoon", "None"),
    VESPERS("Evening", "Vespers"),
    COMPLINE("Night", "Compline");
}

private fun TimeSettings.contains(hour: Int, minute: Int): Boolean {
    if (startTime == null || endTime == null) {
        return false
    }

    // if the end is midnight, just look at start time
    return if (endTime.hour == 0 && endTime.minute == 0) {
        hour > startTime.hour
                || hour == startTime.hour && minute > startTime.minute
    } else {
        hour > startTime.hour && hour < endTime.hour
                || hour == startTime.hour && minute > startTime.minute
                || hour == endTime.hour && minute < endTime.minute
    }

}