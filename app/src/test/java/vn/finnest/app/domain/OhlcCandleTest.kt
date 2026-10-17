package vn.finnest.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OhlcCandleTest {
    @Test fun acceptsValidCandle() {
        assertTrue(OhlcCandle(1L, 10.0, 12.0, 9.0, 11.0, 100L).isValid())
    }

    @Test fun rejectsHighBelowOpen() {
        assertFalse(OhlcCandle(1L, 10.0, 9.0, 8.0, 8.5).isValid())
    }

    @Test fun rejectsNegativeVolume() {
        assertFalse(OhlcCandle(1L, 10.0, 12.0, 9.0, 11.0, -1L).isValid())
    }

    @Test fun sortsCandlesOldestFirst() {
        val later = OhlcCandle(2L, 11.0, 13.0, 10.0, 12.0)
        val earlier = OhlcCandle(1L, 10.0, 12.0, 9.0, 11.0)
        assertEquals(listOf(earlier, later), listOf(later, earlier).validatedOhlc())
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsInvalidSeries() {
        listOf(OhlcCandle(1L, 10.0, 8.0, 7.0, 9.0)).validatedOhlc()
    }
}
