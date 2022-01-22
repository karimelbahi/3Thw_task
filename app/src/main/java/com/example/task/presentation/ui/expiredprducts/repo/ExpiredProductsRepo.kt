package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.room.AppDao


class ExpiredProductsRepo(private val appDao: AppDao) {

    fun getExpiredProducts() = appDao.getExpiredProducts()
}