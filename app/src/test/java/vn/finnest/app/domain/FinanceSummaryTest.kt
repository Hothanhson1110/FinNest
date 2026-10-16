package vn.finnest.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import vn.finnest.app.data.local.HoldingEntity
import vn.finnest.app.data.local.LedgerEntryEntity

class FinanceSummaryTest {
    @Test fun calculatesAssetsProfitLossIncomeAndExpenses() {
        val holdings = listOf(
            HoldingEntity(name = "FPT", symbol = "FPT", assetType = "STOCK",
                quantity = 10.0, averageCostVnd = 100, currentPriceVnd = 120)
        )
        val ledger = listOf(
            LedgerEntryEntity(title = "Salary", amountVnd = 1000, category = "Work", isIncome = true),
            LedgerEntryEntity(title = "Food", amountVnd = 250, category = "Food", isIncome = false)
        )
        val summary = FinanceSummary.calculate(holdings, ledger)
        assertEquals(1200L, summary.assetsVnd)
        assertEquals(200L, summary.profitLossVnd)
        assertEquals(1000L, summary.incomeVnd)
        assertEquals(250L, summary.expenseVnd)
        assertEquals(750L, summary.cashFlowVnd)
        assertEquals(1, summary.holdingCount)
    }
}
