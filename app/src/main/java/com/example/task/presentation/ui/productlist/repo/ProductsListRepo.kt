package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.room.ProductsDao


class ProductsListRepo(private val productsDao: ProductsDao)  {

     fun checkProductsExpiredDateStatus() =
        productsDao.checkProductsExpiredDateStatus().map { product ->
            if (product.expiredDate < System.currentTimeMillis())
                productsDao.checkProductsExpiredDateStatus(product.id)
        }


     fun getProducts() =
        productsDao.getProducts()


     fun getFreshProducts() =productsDao.getFreshProducts()

     fun updateScheduledNotifications() = productsDao.updateScheduledNotifications()


}