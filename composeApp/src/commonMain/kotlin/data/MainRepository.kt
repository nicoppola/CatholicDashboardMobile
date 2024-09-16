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
import networking.DailyDataResponse
import networking.MyClient
import networking.NovusClient
import networking.NovusResponse
import util.NetworkError
import util.Result

interface MainRepository {
    suspend fun retrieveData(): Result<NovusResponse, NetworkError>
    suspend fun myRetrieveData(): Result<DailyDataResponse, NetworkError>
}

class DefaultMainRepository(
    private val remoteData: NovusClient,
    private val myRemoteData: MyClient,
    //private val localData: MainLocalDataSource,
) : MainRepository {

    @OptIn(FormatStringsInDatetimeFormats::class)
    override suspend fun retrieveData(): Result<NovusResponse, NetworkError> {

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            .format(
                LocalDate.Format {
                    byUnicodePattern("yyyy/MM/dd")
                })
        return remoteData.getDay(today)
    }

    override suspend fun myRetrieveData(): Result<DailyDataResponse, NetworkError> =
        with(Dispatchers.IO) {
            return myRemoteData.getToday()
        }
}