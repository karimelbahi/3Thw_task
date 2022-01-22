package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.room.AppDao


class ProductsListRepo(private val appDao: AppDao) {

    fun getProducts() =
        appDao.getProducts()
}