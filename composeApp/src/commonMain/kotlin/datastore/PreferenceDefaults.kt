package datastore

import org.coppola.catholic.OfficeOfReadingsSettings
import org.coppola.catholic.OfficeSettings
import org.coppola.catholic.ReadingsSettings
import org.coppola.catholic.Time
import org.coppola.catholic.TimeSettings

object PreferenceDefaults {
    val readings = ReadingsSettings(
        enabled = true,
    )

    val office = OfficeSettings(
        enabled = true,
        lauds = TimeSettings(
            startTime = Time(hour = 0, minute = 0),
            endTime = Time(hour = 7, minute = 30)
        ),
        prime = null,
        terce = TimeSettings(
            startTime = Time(hour = 7, minute = 30),
            endTime = Time(hour = 10, minute = 30)
        ),
        sext = TimeSettings(
            startTime = Time(hour = 10, minute = 30),
            endTime = Time(hour = 13, minute = 30)
        ),
        none = TimeSettings(
            startTime = Time(hour = 13, minute = 30),
            endTime = Time(hour = 16, minute = 30)
        ),
        vespers = TimeSettings(
            startTime = Time(hour = 16, minute = 30),
            endTime = Time(hour = 19, minute = 30)
        ),
        compline = TimeSettings(
            startTime = Time(hour = 19, minute = 30),
            endTime = Time(hour = 0, minute = 0)
        ),
        matins = null,
    )

    val officeOfReadings = OfficeOfReadingsSettings(
        enabled = true,
    )
}