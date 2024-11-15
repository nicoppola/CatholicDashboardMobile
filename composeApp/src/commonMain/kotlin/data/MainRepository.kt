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
    suspend fun retrieveData(): Result<CalendarData.Day, NetworkError>
    suspend fun retrieveCachedData(): CalendarData.Day?
}

private val lru = mutableMapOf<String, CalendarData.Day>()
@OptIn(FormatStringsInDatetimeFormats::class)
private fun getToday(): String {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
        .format(
            LocalDate.Format {
                byUnicodePattern("dd-MM-yyyy")
            })
}

class DefaultMainRepository(
    private val myRemoteData: MyClient,
) : MainRepository {
    override suspend fun retrieveData(): Result<CalendarData.Day, NetworkError> =
        with(Dispatchers.IO) {
            val today = getToday()
            val cache = lru[today]
            return if (cache != null) {
                return Result.Success(cache)
            } else {
                val result = myRemoteData.getDate(today)
                if (result is Result.Success) {
                    lru[today] = result.data
                }
                result
            }
        }

    override suspend fun retrieveCachedData(): CalendarData.Day? {
        return lru[getToday()]
    }
}