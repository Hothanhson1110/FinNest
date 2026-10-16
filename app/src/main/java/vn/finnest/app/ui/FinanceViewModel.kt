package vn.finnest.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vn.finnest.app.data.local.FinanceDao
import vn.finnest.app.data.local.HoldingEntity
import vn.finnest.app.data.local.LedgerEntryEntity
import vn.finnest.app.domain.FinanceSummary

@HiltViewModel
class FinanceViewModel @Inject constructor(private val dao: FinanceDao) : ViewModel() {
    val holdings = dao.observeHoldings().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val ledger = dao.observeLedger().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val summary = combine(holdings, ledger) { holdingItems, ledgerItems -> FinanceSummary.calculate(holdingItems, ledgerItems) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FinanceSummary(0, 0, 0, 0, 0, 0))

    fun saveHolding(
        name: String, symbol: String, assetType: String,
        quantity: Double, averageCostVnd: Long, currentPriceVnd: Long?
    ) {
        viewModelScope.launch {
            dao.upsertHolding(HoldingEntity(
                name = name.trim(), symbol = symbol.trim().uppercase(), assetType = assetType,
                quantity = quantity, averageCostVnd = averageCostVnd, currentPriceVnd = currentPriceVnd
            ))
        }
    }

    fun deleteHolding(holding: HoldingEntity) { viewModelScope.launch { dao.deleteHolding(holding) } }

    fun saveLedger(title: String, amountVnd: Long, category: String, isIncome: Boolean) {
        viewModelScope.launch {
            dao.insertLedgerEntry(LedgerEntryEntity(
                title = title.trim(), amountVnd = amountVnd, category = category, isIncome = isIncome
            ))
        }
    }

    fun deleteLedger(entry: LedgerEntryEntity) { viewModelScope.launch { dao.deleteLedgerEntry(entry) } }
}
