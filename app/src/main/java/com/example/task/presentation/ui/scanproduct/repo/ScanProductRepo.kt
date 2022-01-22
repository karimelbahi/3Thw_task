package com.example.task.presentation.ui.scanproduct.repo

import com.example.task.data.entity.Product
import com.example.task.data.room.AppDao


class ScanProductRepo(private val appDao: AppDao) {

    suspend fun insertProduct(product: Product) = appDao.insertProduct(product)
}