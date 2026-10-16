package vn.finnest.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val quantity: Double,
    val averageCost: Double,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "ledger_entries")
data class LedgerEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amountVnd: Long,
    val category: String,
    val isIncome: Boolean,
    val occurredAtEpochMillis: Long = System.currentTimeMillis()
)
