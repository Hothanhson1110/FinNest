package vn.finnest.app.domain

import vn.finnest.app.data.local.HoldingEntity
import vn.finnest.app.data.local.LedgerEntryEntity

data class FinanceSummary(
    val assetsVnd: Long,
    val investedVnd: Long,
    val profitLossVnd: Long,
    val incomeVnd: Long,
    val expenseVnd: Long,
    val holdingCount: Int
) {
    val cashFlowVnd: Long get() = incomeVnd - expenseVnd

    companion object {
        fun calculate(holdings: List<HoldingEntity>, entries: List<LedgerEntryEntity>): FinanceSummary =
            FinanceSummary(
                assetsVnd = holdings.sumOf { it.marketValueVnd() },
                investedVnd = holdings.sumOf { it.investedValueVnd() },
                profitLossVnd = holdings.sumOf { it.profitLossVnd() },
                incomeVnd = entries.filter { it.isIncome }.sumOf { it.amountVnd },
                expenseVnd = entries.filterNot { it.isIncome }.sumOf { it.amountVnd },
                holdingCount = holdings.size
            )
    }
}
