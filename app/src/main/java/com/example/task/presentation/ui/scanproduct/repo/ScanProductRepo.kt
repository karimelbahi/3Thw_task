package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.entity.Product
import com.example.task.data.room.ProductsDao


class ScanProductRepo(private val productsDao: ProductsDao) {

    suspend fun insertProduct(product: Product) = productsDao.insertProduct(product)
}