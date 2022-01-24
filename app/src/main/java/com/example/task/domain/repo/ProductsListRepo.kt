package com.example.task.domain.repo

import com.example.task.data.database.entity.Product
import kotlinx.coroutines.flow.Flow


interface ProductsListRepo {

    fun checkProductsExpiredDateStatus()
    fun getProducts(): Flow<List<Product>>
    fun getFreshProducts(): List<Product>
    fun updateScheduledNotifications(): Int


}