package vn.finnest.app.domain

/** OHLC candle. Prices are in the same unit and timestamps are epoch milliseconds. */
data class OhlcCandle(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long = 0L
) {
    fun isValid(): Boolean =
        open.isFinite() && high.isFinite() && low.isFinite() && close.isFinite() &&
            open > 0.0 && high > 0.0 && low > 0.0 && close > 0.0 &&
            high >= maxOf(open, close, low) &&
            low <= minOf(open, close, high) &&
            volume >= 0L
}

fun List<OhlcCandle>.validatedOhlc(): List<OhlcCandle> {
    require(all { it.isValid() }) { "Dữ liệu OHLC không hợp lệ." }
    return sortedBy { it.timestamp }
}
