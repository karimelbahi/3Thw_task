package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.room.AppDao


class ProductsListRepo(private val appDao: AppDao) {

    fun checkProductsExpiredDateStatus() {
        appDao.checkProductsExpiredDateStatus().map { product ->
            if (product.expiredDate < System.currentTimeMillis())
                appDao.checkProductsExpiredDateStatus(product.id)
        }
    }

    fun getProducts() = appDao.getProducts()

    fun getFreshProducts() = appDao.getFreshProducts()

    fun updateScheduledNotifications() = appDao.updateScheduledNotifications()

}