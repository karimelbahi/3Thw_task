package com.example.myutils.di

import com.example.task.data.room.ProductsDao
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
        productsDao: ProductsDao,
    ): ProductsListRepo {
        return ProductsListRepo(productsDao)
    }

    @Singleton
    @Provides
    fun provideScanProductRepository(
        productsDao: ProductsDao,
    ): ScanProductRepo {
        return ScanProductRepo(productsDao)
    }

    @Singleton
    @Provides
    fun provideExpiredProductsRepository(
        productsDao: ProductsDao,
    ): ExpiredProductsRepo {
        return ExpiredProductsRepo(productsDao)
    }
}