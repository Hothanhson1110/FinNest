package vn.finnest.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HoldingEntity::class, LedgerEntryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class FinNestDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao
}
