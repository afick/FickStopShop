package hu.ait.fickstopshop.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.fickstopshop.data.ShopAppDatabase
import hu.ait.fickstopshop.data.ShopDAO
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideShopDao(appDatabase: ShopAppDatabase): ShopDAO {
        return appDatabase.shopDao()
    }

    @Provides
    @Singleton
    fun provideShopAppDatabase(@ApplicationContext appContext: Context): ShopAppDatabase {
        return ShopAppDatabase.getDatabase(appContext)
    }
}