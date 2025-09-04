package data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DayData(
    val id: LocalDate?,
    val date: String,
    val title: String,
    val secondaryCelebrations: List<SecondaryCelebrationSection> = emptyList(),
//    val color: Color?,
    val readingSections: ReadingsSection,
    val liturgyHours: LiturgyHoursSection,
    val rosary: RosarySection,
) {

    @Serializable
    data class RosarySection(
        val sectionTitle: String = "",
        val rosary: Rosary,
    )

    @Serializable
    data class SecondaryCelebrationSection(
        val rankTitle: String,
        val titles: List<String>,
    )

    @Serializable
    data class ReadingsSection(
        val sectionTitle: String = "",
        val subSections: List<ReadingsSubSection> = emptyList(),
    )

    @Serializable
    data class ReadingsSubSection(
        val sectionSubtitle: String? = "",
        val link: String = "",
        val readingTitles: List<Readings> = emptyList()
    ) {
        @Serializable
        data class Readings(
            val title: String,
            val verses: String,
        )
    }

    @Serializable
    data class LiturgyHoursSection(
        val mainLink: String = "",
        val sectionTitle: String = "",
        val hours: List<Hour> = emptyList(),
    ) {
        @Serializable
        data class Hour(
            val id: HourId,
            val link: String,
        )
    }
}