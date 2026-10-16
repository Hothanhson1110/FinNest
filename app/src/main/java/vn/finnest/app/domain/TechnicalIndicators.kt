package vn.finnest.app.domain

/**
 * Pure, deterministic indicators. Input prices must be ordered oldest -> newest.
 * These indicators are educational tools, not personalized investment advice.
 */
object TechnicalIndicators {
    fun sma(values: List<Double>, period: Int): List<Double?> {
        require(period > 0) { "period must be positive" }
        return values.indices.map { index ->
            if (index + 1 < period) null
            else values.subList(index + 1 - period, index + 1).average()
        }
    }

    fun rsi(closes: List<Double>, period: Int = 14): List<Double?> {
        require(period > 0) { "period must be positive" }
        if (closes.isEmpty()) return emptyList()
        val result = MutableList<Double?>(closes.size) { null }
        if (closes.size <= period) return result
        for (i in period until closes.size) {
            var gains = 0.0
            var losses = 0.0
            for (j in (i - period + 1)..i) {
                val change = closes[j] - closes[j - 1]
                if (change > 0) gains += change else losses -= change
            }
            result[i] = when {
                losses == 0.0 && gains == 0.0 -> 50.0
                losses == 0.0 -> 100.0
                else -> 100.0 - (100.0 / (1.0 + gains / losses))
            }
        }
        return result
    }
}
