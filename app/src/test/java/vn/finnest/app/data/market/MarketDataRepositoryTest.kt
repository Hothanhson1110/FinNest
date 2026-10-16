package vn.finnest.app.data.market

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketDataRepositoryTest {
    @Test fun rejectsEmptyAndInsecureUrls() {
        assertFalse(MarketDataRepository.isValidBaseUrl(""))
        assertFalse(MarketDataRepository.isValidBaseUrl("http://market.example.com/"))
        assertFalse(MarketDataRepository.isValidBaseUrl("https://user:pass@market.example.com/"))
    }

    @Test fun acceptsHttpsBackendUrl() {
        assertTrue(MarketDataRepository.isValidBaseUrl("https://market.example.com/"))
        assertTrue(MarketDataRepository.isValidBaseUrl("https://market.example.com"))
    }
}
