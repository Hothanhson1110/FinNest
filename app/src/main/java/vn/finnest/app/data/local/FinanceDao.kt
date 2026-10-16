package vn.finnest.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM holdings ORDER BY name COLLATE NOCASE ASC")
    fun observeHoldings(): Flow<List<HoldingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHolding(holding: HoldingEntity): Long

    @Delete
    suspend fun deleteHolding(holding: HoldingEntity)

    @Query("SELECT * FROM ledger_entries ORDER BY occurredAtEpochMillis DESC")
    fun observeLedger(): Flow<List<LedgerEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLedgerEntry(entry: LedgerEntryEntity): Long

    @Delete
    suspend fun deleteLedgerEntry(entry: LedgerEntryEntity)
}
