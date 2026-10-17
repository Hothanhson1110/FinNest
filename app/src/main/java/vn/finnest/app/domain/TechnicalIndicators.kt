package vn.finnest.app.domain

/**
 * Pure technical-indicator calculations. Inputs must be ordered oldest -> newest.
 * These functions do not fetch prices and must not be treated as trading advice.
 */
object TechnicalIndicators {
    data class MacdPoint(
        val macd: Double?,
        val signal: Double?,
        val histogram: Double?
    )

    fun sma(values: List<Double>, period: Int): List<Double?> {
        require(period > 0) { "period must be positive" }
        return values.indices.map { i ->
            if (i + 1 < period) null else values.subList(i + 1 - period, i + 1).average()
        }
    }

    fun ema(values: List<Double>, period: Int): List<Double?> {
        require(period > 0) { "period must be positive" }
        val result = MutableList<Double?>(values.size) { null }
        if (values.size < period) return result

        var current = values.take(period).average()
        result[period - 1] = current
        val alpha = 2.0 / (period + 1.0)
        for (i in period until values.size) {
            current = values[i] * alpha + current * (1.0 - alpha)
            result[i] = current
        }
        return result
    }

    /**
     * RSI using the simple average gain/loss over the selected lookback.
     * The first value is available after [period] price changes.
     */
    fun rsi(closes: List<Double>, period: Int = 14): List<Double?> {
        require(period > 0) { "period must be positive" }
        val result = MutableList<Double?>(closes.size) { null }
        if (closes.size <= period) return result
        for (i in period until closes.size) {
            var gain = 0.0
            var loss = 0.0
            for (j in (i - period + 1)..i) {
                val delta = closes[j] - closes[j - 1]
                if (delta > 0) gain += delta else loss -= delta
            }
            result[i] = when {
                gain == 0.0 && loss == 0.0 -> 50.0
                loss == 0.0 -> 100.0
                gain == 0.0 -> 0.0
                else -> 100.0 - 100.0 / (1.0 + gain / loss)
            }
        }
        return result
    }

    /** MACD = EMA(fast) - EMA(slow); signal is EMA(signalPeriod) of MACD values. */
    fun macd(
        closes: List<Double>,
        fastPeriod: Int = 12,
        slowPeriod: Int = 26,
        signalPeriod: Int = 9
    ): List<MacdPoint> {
        require(fastPeriod > 0 && slowPeriod > 0 && signalPeriod > 0) {
            "periods must be positive"
        }
        require(fastPeriod < slowPeriod) { "fastPeriod must be smaller than slowPeriod" }

        val fast = ema(closes, fastPeriod)
        val slow = ema(closes, slowPeriod)
        val macdValues = closes.indices.map { i ->
            val f = fast[i]
            val sl = slow[i]
            if (f == null || sl == null) null else f - sl
        }

        val firstMacdIndex = macdValues.indexOfFirst { it != null }
        val compactMacd = macdValues.drop(firstMacdIndex).mapNotNull { it }
        val compactSignal = ema(compactMacd, signalPeriod)
        val signalByIndex = MutableList<Double?>(closes.size) { null }
        compactSignal.forEachIndexed { offset, value ->
            signalByIndex[firstMacdIndex + offset] = value
        }

        return closes.indices.map { i ->
            val m = macdValues[i]
            val signal = signalByIndex[i]
            MacdPoint(m, signal, if (m != null && signal != null) m - signal else null)
        }
    }
}
