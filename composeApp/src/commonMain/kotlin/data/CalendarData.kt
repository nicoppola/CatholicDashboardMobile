package data

import kotlinx.serialization.Serializable

@Serializable
data class CalendarData(
    val year: String?,
    val months: Map<Int, MutableMap<Int, Day>>? = emptyMap(),
){
    @Serializable
    data class Day(
        val date: String? = "",
        var title: String? = "",
        var color: Color? = Color.UNDEFINED, //todo, not correct rn
        val readings: List<Readings> = emptyList(),
        val office: LiturgyHours? = null,
        val propers: List<Proper> = emptyList(),
    )
    @Serializable
    data class LiturgyHours(
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
        val link: String?,
        val readingOne: String?,
        val psalm: String?,
        val readingTwo: String? = null,
        val gospel: String?,
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