package com.example.task.data.repo

import com.example.task.data.database.model.Product
import com.example.task.data.database.model.room.ProductsDao
import com.example.task.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(private val productsDao: ProductsDao) :
    ProductsRepo {

    override fun checkProductsExpiredDateStatus() {
        productsDao.checkProductsExpiredDateStatus().map { product ->
            if (product.expiredDate < System.currentTimeMillis())
                productsDao.checkProductsExpiredDateStatus(product.id)
        }
    }
    override fun getProducts(): Flow<List<Product>> = productsDao.getProducts()
    override fun getFreshProducts(): List<Product> = productsDao.getFreshProducts()
    override fun updateScheduledNotifications(): Int = productsDao.updateScheduledNotifications()

    override fun getExpiredProducts() = productsDao.getExpiredProducts()

    override suspend fun insertProduct(product: Product) = productsDao.insertProduct(product)



}