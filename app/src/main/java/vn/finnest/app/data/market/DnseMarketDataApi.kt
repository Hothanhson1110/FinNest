package vn.finnest.app.data.market

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Integration seam only. Confirm the current DNSE endpoint, schema, and authentication
 * requirements from https://developers.dnse.com.vn before implementing real requests.
 * Never place API Secret in this Android client.
 */
interface DnseMarketDataApi {
    @GET("v1/market-data/ohlc")
    suspend fun getOhlc(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: String,
        @Query("from") fromEpochSeconds: Long,
        @Query("to") toEpochSeconds: Long
    ): OhlcResponse
}

data class OhlcResponse(val data: List<OhlcBar> = emptyList())
data class OhlcBar(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)
