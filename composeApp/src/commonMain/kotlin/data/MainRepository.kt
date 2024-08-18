package data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.todayIn
import networking.NovusClient
import networking.NovusResponse
import util.NetworkError
import util.Result

interface MainRepository {
    suspend fun retrieveData(): Result<NovusResponse, NetworkError>
}

class DefaultMainRepository(
    private val remoteData: NovusClient,
    //private val localData: MainLocalDataSource,
) : MainRepository {

    @OptIn(FormatStringsInDatetimeFormats::class)
    override suspend fun retrieveData(): Result<NovusResponse, NetworkError> {

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            .format(
                LocalDate.Format {
                    byUnicodePattern("yyyy/MM/dd")
                } )
            return remoteData.getDay(today)
        }
    }