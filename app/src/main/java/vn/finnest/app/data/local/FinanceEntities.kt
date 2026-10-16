package vn.finnest.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val symbol: String = "",
    val assetType: String, // STOCK, CASH, GOLD, FUND, OTHER
    val quantity: Double,
    val averageCostVnd: Long,
    val currentPriceVnd: Long? = null,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
) {
    fun investedValueVnd(): Long = (quantity * averageCostVnd).toLong()
    fun marketValueVnd(): Long = (quantity * (currentPriceVnd ?: averageCostVnd)).toLong()
    fun profitLossVnd(): Long = marketValueVnd() - investedValueVnd()
}

@Entity(tableName = "ledger_entries")
data class LedgerEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amountVnd: Long,
    val category: String,
    val isIncome: Boolean,
    val occurredAtEpochMillis: Long = System.currentTimeMillis()
)
