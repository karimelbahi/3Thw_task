package com.example.task.data

import com.example.task.data.database.model.Product
import com.example.task.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow

class FakeScanProductRepoTest :ProductsRepo {
    override fun checkProductsExpiredDateStatus() {
        TODO("Not yet implemented")
    }

    override fun getProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getFreshProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override fun updateScheduledNotifications(): Int {
        TODO("Not yet implemented")
    }

    override fun getExpiredProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertProduct(product: Product): Long {
        TODO("Not yet implemented")
    }
}