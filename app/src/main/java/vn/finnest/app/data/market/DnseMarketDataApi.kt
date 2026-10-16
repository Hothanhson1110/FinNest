package vn.finnest.app.data.market

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * FinNest's own read-only backend contract. This is NOT a direct DNSE endpoint.
 * The backend is responsible for DNSE authentication and must never expose API secrets.
 */
interface DnseMarketDataApi {
    @GET("api/v1/market/quotes")
    suspend fun getQuotes(@Query("symbols") symbols: String): MarketQuotesResponse
}

data class MarketQuotesResponse(val data: List<MarketQuoteDto> = emptyList())

data class MarketQuoteDto(
    val symbol: String,
    val price: Double,
    val change: Double = 0.0,
    val changePercent: Double = 0.0,
    val currency: String = "VND",
    val updatedAt: String? = null
)
