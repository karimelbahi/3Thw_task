package com.example.myutils.di

import com.example.task.data.room.AppDao
import com.example.task.presentation.ui.scanproduct.repo.ScanProductRepo
import com.example.task.presentation.ui.scanproduct.repo.ExpiredProductsRepo
import com.example.task.presentation.ui.scanproduct.repo.ProductsListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideProductListRepository(
        appDao: AppDao,
    ): ProductsListRepo {
        return ProductsListRepo(appDao)
    }

    @Singleton
    @Provides
    fun provideScanProductRepository(
        appDao: AppDao,
    ): ScanProductRepo {
        return ScanProductRepo(appDao)
    }

    @Singleton
    @Provides
    fun provideExpiredProductsRepository(
        appDao: AppDao,
    ): ExpiredProductsRepo {
        return ExpiredProductsRepo(appDao)
    }
}