package domain

import data.CalendarData
import data.MainRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.main.ListCollectionItemUiState
import ui.main.ListCollectionUiState
import ui.main.TextRow

class GetLiturgyOfHoursListItemUseCase(
    private val mainRepository: MainRepository,
) {
    suspend operator fun invoke(
        date: LocalDate,
        isExpanded: Boolean = false
    ): ListCollectionUiState {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

        val office = mainRepository.retrieveCachedData(date)?.office

        return ListCollectionUiState(
            header = "Liturgy of the Hours",
            isExpanded = isExpanded,
            items = LiturgyHours.entries.map {
                ListCollectionItemUiState(
                    subHeader = null,
                    rows = listOf(
                        TextRow(
                            title = "${it.novusLabel}:",
                            text = it.getTimeText(use24HourTime = false)
                        )
                    ),
                    link = it.getLink(office),
                    showOnCollapsed = it.contains(now),
                )
            }
        )
    }

    private fun LiturgyHours.contains(now: LocalTime): Boolean {
        return this.timeStart.isEarlierOrEqualTo(now) && this.timeEnd.isLaterOrEqualTo(now)
    }

    private fun LocalTime.isEarlierOrEqualTo(time: LocalTime): Boolean {
        return this <= time
    }

    private fun LocalTime.isLaterOrEqualTo(time: LocalTime): Boolean {
        return this >= time
    }

    private fun LiturgyHours.getTimeText(
        use24HourTime: Boolean = false,
    ): String {
        val formattedStart: String
        val formattedEnd: String

        if (use24HourTime) {
            formattedStart = this.timeStart.to24HourTime()
            formattedEnd = this.timeEnd.to24HourTime()
        } else {
            formattedStart = this.timeStart.toMeridianTime()
            formattedEnd = this.timeEnd.toMeridianTime()
        }

        return "$formattedStart - $formattedEnd"
    }

    private fun LocalTime.to24HourTime(): String {
        return this.hour.toString().padStart(2, '0') + ":" + this.minute.toString().padStart(2, '0')
    }

    private fun LocalTime.toMeridianTime(): String {
        val hour12 = if (this.hour % 12 == 0) 12 else this.hour % 12
        val amPm = if (this.hour < 12) "am" else "pm"
        return hour12.toString() + ":" + this.minute.toString().padStart(2, '0') + amPm

    }

    private fun LiturgyHours.getLink(data: CalendarData.LiturgyHours?): String? {
        if (data == null) {
            return ""
        }
        return when (this) {
            LiturgyHours.LAUDS -> data.morning
            LiturgyHours.TERCE -> data.midMorning
            LiturgyHours.SEXT -> data.midday
            LiturgyHours.NONE -> data.midAfternoon
            LiturgyHours.VESPERS -> data.evening
            LiturgyHours.COMPLINE -> data.night
        }
    }

    private enum class LiturgyHours(
        val novusLabel: String,
        val latinLabel: String,
        val timeStart: LocalTime,
        val timeEnd: LocalTime,
    ) {
        LAUDS(
            novusLabel = "Morning",
            latinLabel = "Lauds",
            timeStart = LocalTime(0, 0),
            timeEnd = LocalTime(7, 30)
        ),
        TERCE(
            novusLabel = "Mid-morning",
            latinLabel = "Terce",
            timeStart = LocalTime(7, 30),
            timeEnd = LocalTime(10, 30)
        ),
        SEXT(
            novusLabel = "Midday",
            latinLabel = "Sext",
            timeStart = LocalTime(10, 30),
            timeEnd = LocalTime(13, 30)
        ),
        NONE(
            novusLabel = "Mid-afternoon",
            latinLabel = "None",
            timeStart = LocalTime(13, 30),
            timeEnd = LocalTime(16, 30)
        ),
        VESPERS(
            novusLabel = "Evening",
            latinLabel = "Vespers",
            timeStart = LocalTime(16, 30),
            timeEnd = LocalTime(19, 30)
        ),
        COMPLINE(
            novusLabel = "Night",
            latinLabel = "Compline",
            timeStart = LocalTime(19, 30),
            timeEnd = LocalTime(23, 59)
        );
    }
}
