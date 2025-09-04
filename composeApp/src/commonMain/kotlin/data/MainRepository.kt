package data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import networking.MyClient
import util.NetworkError
import util.Result

private val lru = mutableMapOf<String, DayData>()

@OptIn(FormatStringsInDatetimeFormats::class)
private fun LocalDate.apiFormat(): String {
    return LocalDate.Format {
        byUnicodePattern("dd-MM-yyyy")
    }.format(this)
}

class V2MainRepository(
    private val myRemoteData: MyClient,
) {
    suspend fun retrieveData(date: LocalDate): util.Result<DayData, NetworkError> =
        with(Dispatchers.IO) {
            val formattedDate = date.apiFormat()
            val cache = lru[formattedDate]
            return if (cache != null) {
                return util.Result.Success(cache)
            } else {
                val result = myRemoteData.getData(formattedDate)
                if (result is Result.Success) {
                    lru[formattedDate] = result.data
                }
                result
            }
        }

    suspend fun retrieveCachedData(date: LocalDate): DayData? {
        val formattedDate = date.apiFormat()
        return lru[formattedDate]
    }
}