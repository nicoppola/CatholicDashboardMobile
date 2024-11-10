package datastore

import org.coppola.catholic.OfficeOfReadingsPrefs
import org.coppola.catholic.OfficePrefs
import org.coppola.catholic.ReadingsPrefs
import org.coppola.catholic.TimePref
import org.coppola.catholic.TimeRangePrefs

object PreferenceDefaults {
    val readings = ReadingsPrefs(
        enabled = true,
    )

    val office = OfficePrefs(
        enabled = true,
        lauds = TimeRangePrefs(
            startTime = TimePref(hour = 0, minute = 0),
            endTime = TimePref(hour = 7, minute = 30)
        ),
        prime = null,
        terce = TimeRangePrefs(
            startTime = TimePref(hour = 7, minute = 30),
            endTime = TimePref(hour = 10, minute = 30)
        ),
        sext = TimeRangePrefs(
            startTime = TimePref(hour = 10, minute = 30),
            endTime = TimePref(hour = 13, minute = 30)
        ),
        none = TimeRangePrefs(
            startTime = TimePref(hour = 13, minute = 30),
            endTime = TimePref(hour = 16, minute = 30)
        ),
        vespers = TimeRangePrefs(
            startTime = TimePref(hour = 16, minute = 30),
            endTime = TimePref(hour = 19, minute = 30)
        ),
        compline = TimeRangePrefs(
            startTime = TimePref(hour = 19, minute = 30),
            endTime = TimePref(hour = 0, minute = 0)
        ),
        matins = null,
    )

    val officeOfReadings = OfficeOfReadingsPrefs(
        enabled = true,
    )
}