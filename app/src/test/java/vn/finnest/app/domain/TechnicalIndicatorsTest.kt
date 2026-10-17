package vn.finnest.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TechnicalIndicatorsTest {
    @Test fun smaReturnsNullUntilWindowIsFull() {
        val result = TechnicalIndicators.sma(listOf(1.0, 2.0, 3.0, 4.0), 3)
        assertNull(result[0]); assertNull(result[1])
        assertEquals(2.0, result[2]!!, 0.0001)
        assertEquals(3.0, result[3]!!, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun smaRejectsInvalidPeriod() { TechnicalIndicators.sma(listOf(1.0), 0) }

    @Test fun emaStartsAtSimpleAverageThenSmooths() {
        val result = TechnicalIndicators.ema(listOf(10.0, 11.0, 12.0, 13.0), 3)
        assertNull(result[0]); assertNull(result[1])
        assertEquals(11.0, result[2]!!, 0.0001)
        assertEquals(12.0, result[3]!!, 0.0001)
    }

    @Test fun rsiIs100WhenAllChangesArePositive() {
        assertEquals(100.0, TechnicalIndicators.rsi((1..16).map { it.toDouble() }, 14).last()!!, 0.0001)
    }

    @Test fun rsiIsZeroWhenAllChangesAreNegative() {
        val closes = (20 downTo 1).map { it.toDouble() }
        assertEquals(0.0, TechnicalIndicators.rsi(closes, 14).last()!!, 0.0001)
    }

    @Test fun macdWaitsUntilSlowEmaAndSignalAreAvailable() {
        val closes = (1..40).map { it.toDouble() }
        val result = TechnicalIndicators.macd(closes)
        assertNull(result[24].macd)
        assertNull(result[32].signal)
        assertEquals(result.last().macd!! - result.last().signal!!, result.last().histogram!!, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun macdRejectsFastPeriodNotSmallerThanSlowPeriod() {
        TechnicalIndicators.macd(listOf(1.0, 2.0), fastPeriod = 10, slowPeriod = 5)
    }
}
