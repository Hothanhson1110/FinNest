package vn.finnest.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import vn.finnest.app.BuildConfig
import vn.finnest.app.data.market.MarketDataRepository

@Module
@InstallIn(SingletonComponent::class)
object MarketDataModule {
    @Provides
    @Singleton
    fun provideMarketDataRepository(): MarketDataRepository =
        MarketDataRepository(BuildConfig.MARKET_API_BASE_URL)
}
