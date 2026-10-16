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
    @Test fun rsiIs100WhenAllChangesArePositive() {
        assertEquals(100.0, TechnicalIndicators.rsi((1..16).map { it.toDouble() }, 14).last()!!, 0.0001)
    }
}
