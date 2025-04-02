package data

import kotlinx.serialization.Serializable

@Serializable
data class CalendarData(
    val year: String,
    val months: MutableMap<Int, MutableMap<Int, Day>>,
){
    @Serializable
    data class Day(
        val date: String,
        var title: String,
        var color: Color,
        val readings: Readings,
        val office: Office,
        val propers: MutableList<Proper>,
    )
    @Serializable
    data class Office(
        val link: String?,
        val morning: String?,
        val midMorning: String?,
        val midday: String?,
        val midAfternoon: String?,
        val evening: String?,
        val night: String?,
        val officeOfReadings: String?,
        val officeOfReadingsSubtitle: String? = "",
    )

    @Serializable
    data class Readings(
        val link: String,
        val readingOne: String,
        val psalm: String,
        val readingTwo: String? = null,
        val gospel: String,
        val title: String? = null,
    )

    @Serializable
    data class Proper(
        val key: String?,
        val rank: Rank?,
        val title: String?,
    )

    enum class Color(value: String){
        GREEN("green"),
        PURPLE("purple"),
        ROSE("rose"),
        WHITE("white"),
        RED("red"),
        UNDEFINED("undefined"),
    }
    enum class Rank(value: String, rank: Int) {
        UNKNOWN("undefined", 0),
        SOLEMNITY("Solemnity", 1),
        SUNDAY("Sunday", 2),
        FEAST("Feast", 3),
        MEMORIAL("Memorial", 4),
        OPTIONAL_MEMORIAL("Optional Memorial", 5),
        FERIA("Feria", 6);
    }
}