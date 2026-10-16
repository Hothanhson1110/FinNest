package vn.finnest.app.domain

object TechnicalIndicators {
    fun sma(values: List<Double>, period: Int): List<Double?> {
        require(period > 0) { "period must be positive" }
        return values.indices.map { i ->
            if (i + 1 < period) null else values.subList(i + 1 - period, i + 1).average()
        }
    }

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
                else -> 100.0 - 100.0 / (1.0 + gain / loss)
            }
        }
        return result
    }
}
