package networking

import data.CalendarData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import util.NetworkError
import util.Result

interface MyClient {
    suspend fun getDate(date: String): Result<CalendarData.Day, NetworkError>
}

class DefaultMyClient(
    private val httpClient: HttpClient
) : MyClient {

    override suspend fun getDate(date: String): Result<CalendarData.Day, NetworkError> {
        val response = try {
            httpClient.get(
                //ie date = "2015/6/27"
                urlString = "https://catholicdashboardapi.onrender.com/novus/$date"
//                urlString = "http://10.0.2.2:8080/novus/$date"
            )
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: Exception) {
            //java.net.ConnectException: Failed to connect to /10.0.2.2:8080
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val result = response.body<CalendarData.Day>()
                Result.Success(result)
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}