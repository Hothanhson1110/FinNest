package vn.finnest.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import vn.finnest.app.data.local.FinNestDatabase
import vn.finnest.app.data.local.FinanceDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FinNestDatabase =
        Room.databaseBuilder(context, FinNestDatabase::class.java, "finnest.db")
            .build()

    @Provides
    fun provideFinanceDao(database: FinNestDatabase): FinanceDao = database.financeDao()
}
