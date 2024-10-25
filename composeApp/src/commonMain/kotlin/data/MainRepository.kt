package data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.todayIn
import networking.MyClient
import util.NetworkError
import util.Result

interface MainRepository {
    suspend fun myRetrieveData(): Result<CalendarData.Day, NetworkError>
}

class DefaultMainRepository(
    private val myRemoteData: MyClient,
) : MainRepository {
    @OptIn(FormatStringsInDatetimeFormats::class)
    override suspend fun myRetrieveData(): Result<CalendarData.Day, NetworkError> =
        with(Dispatchers.IO) {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .format(
                    LocalDate.Format {
                        byUnicodePattern("dd-MM-yyyy")
                    })

            return myRemoteData.getDate(today)
        }
}