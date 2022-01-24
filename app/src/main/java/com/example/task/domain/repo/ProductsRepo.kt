package com.example.task.domain.repo

import com.example.task.data.database.model.Product
import kotlinx.coroutines.flow.Flow


interface ProductsRepo {

    fun checkProductsExpiredDateStatus()
    fun getProducts(): Flow<List<Product>>
    fun getFreshProducts(): List<Product>
    fun updateScheduledNotifications(): Int

    fun getExpiredProducts(): Flow<List<Product>>


    suspend fun insertProduct(product: Product) : Long



}