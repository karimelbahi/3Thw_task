package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.database.entity.Product
import kotlinx.coroutines.flow.Flow


interface ExpiredProductsRepo {

    fun getExpiredProducts(): Flow<List<Product>>
}