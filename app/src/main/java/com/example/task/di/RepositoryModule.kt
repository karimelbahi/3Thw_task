package com.example.myutils.di

import com.example.task.data.database.entity.room.ProductsDao
import com.example.task.data.repo.ExpiredProductsRepoImpl
import com.example.task.data.repo.ProductListRepoImpl
import com.example.task.domain.repo.ScanProductRepo
import com.example.task.presentation.ui.scanproduct.repo.ExpiredProductsRepo
import com.example.task.domain.repo.ProductsListRepo
import com.example.task.data.repo.ScanProductRepoImpl
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
        productsDao: ProductsDao
    ): ProductsListRepo {
        return ProductListRepoImpl(productsDao)
    }

    @Singleton
    @Provides
    fun provideScanProductRepository(
        productsDao: ProductsDao
    ): ScanProductRepo {
        return ScanProductRepoImpl(productsDao)
    }

    @Singleton
    @Provides
    fun provideExpiredProductsRepository(
        productsDao: ProductsDao
    ): ExpiredProductsRepo {
        return ExpiredProductsRepoImpl(productsDao)
    }
}