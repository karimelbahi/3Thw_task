package com.example.task.domain.repo

import com.example.task.data.database.entity.Product


interface ScanProductRepo {

    suspend fun insertProduct(product: Product) : Long
}