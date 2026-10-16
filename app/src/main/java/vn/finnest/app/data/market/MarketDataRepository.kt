package vn.finnest.app.data.market

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarketDataRepository(private val baseUrl: String) {
    fun isConfigured(): Boolean = isValidBaseUrl(baseUrl)

    suspend fun quotes(symbols: List<String>): List<MarketQuoteDto> {
        require(symbols.isNotEmpty()) { "Vui lòng nhập ít nhất một mã chứng khoán." }
        check(isConfigured()) {
            "Chưa cấu hình MARKET_API_BASE_URL. FinNest cần backend trung gian an toàn trước khi gọi DNSE."
        }
        val normalized = symbols.map { it.trim().uppercase() }.filter { it.matches(Regex("[A-Z0-9]{2,12}")) }.distinct()
        require(normalized.isNotEmpty()) { "Mã chứng khoán không hợp lệ." }
        return api().getQuotes(normalized.joinToString(",")).data
    }

    private fun api(): DnseMarketDataApi = Retrofit.Builder()
        .baseUrl(baseUrl.trim().let { if (it.endsWith('/')) it else "$it/" })
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DnseMarketDataApi::class.java)

    companion object {
        fun isValidBaseUrl(value: String): Boolean = try {
            val uri = java.net.URI(value.trim())
            (uri.scheme == "https") && !uri.host.isNullOrBlank() && uri.userInfo == null
        } catch (_: Exception) { false }
    }
}
