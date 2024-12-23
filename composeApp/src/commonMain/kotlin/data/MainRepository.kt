package data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import networking.MyClient
import util.NetworkError
import util.Result

interface MainRepository {
    suspend fun retrieveData(date: LocalDate): Result<CalendarData.Day, NetworkError>
    suspend fun retrieveCachedData(date: LocalDate): CalendarData.Day?
}

private val lru = mutableMapOf<String, CalendarData.Day>()

@OptIn(FormatStringsInDatetimeFormats::class)
private fun LocalDate.apiFormat(): String {
    return LocalDate.Format {
        byUnicodePattern("dd-MM-yyyy")
    }.format(this)
}

class DefaultMainRepository(
    private val myRemoteData: MyClient,
) : MainRepository {
    override suspend fun retrieveData(date: LocalDate): Result<CalendarData.Day, NetworkError> =
        with(Dispatchers.IO) {
            val formattedDate = date.apiFormat()
            val cache = lru[formattedDate]
            return if (cache != null) {
                return Result.Success(cache)
            } else {
                val result = myRemoteData.getDate(formattedDate)
                if (result is Result.Success) {
                    lru[formattedDate] = result.data
                }
                result
            }
        }

    override suspend fun retrieveCachedData(date: LocalDate): CalendarData.Day? {
        val formattedDate = date.apiFormat()
        return lru[formattedDate]
    }
}