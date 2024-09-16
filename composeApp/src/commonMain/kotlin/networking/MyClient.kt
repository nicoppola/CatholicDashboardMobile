package networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import util.Result
import util.NetworkError

interface MyClient {
    suspend fun getToday(): Result<DailyDataResponse, NetworkError>
}

class DefaultMyClient(
    private val httpClient: HttpClient
) : MyClient{

    override suspend fun getToday(): Result<DailyDataResponse, NetworkError> {
        val response = try{
            httpClient.get(
                //ie date = "2015/6/27"
                urlString = "http://10.0.2.2:8080/today"
            )
        } catch (e: UnresolvedAddressException){
            return Result.Error(NetworkError.NO_INTERNET)
        } catch(e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val result = response.body<DailyDataResponse>()
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