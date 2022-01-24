package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.room.ProductsDao


class ExpiredProductsRepo(private val productsDao: ProductsDao) {

    fun getExpiredProducts() = productsDao.getExpiredProducts()
}