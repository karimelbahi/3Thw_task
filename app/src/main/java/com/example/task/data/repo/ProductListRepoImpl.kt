package com.example.task.data.repo

import com.example.task.data.database.entity.Product
import com.example.task.data.database.entity.room.ProductsDao
import com.example.task.domain.repo.ProductsListRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListRepoImpl @Inject constructor(private val productsDao: ProductsDao) :
    ProductsListRepo {

    override fun checkProductsExpiredDateStatus() {
        productsDao.checkProductsExpiredDateStatus().map { product ->
            if (product.expiredDate < System.currentTimeMillis())
                productsDao.checkProductsExpiredDateStatus(product.id)
        }
    }

    override fun getProducts(): Flow<List<Product>> = productsDao.getProducts()
    override fun getFreshProducts(): List<Product> = productsDao.getFreshProducts()
    override fun updateScheduledNotifications(): Int = productsDao.updateScheduledNotifications()

}