package com.example.task.data.repo

import com.example.task.data.database.entity.room.ProductsDao
import com.example.task.presentation.ui.scanproduct.repo.ExpiredProductsRepo
import javax.inject.Inject

class ExpiredProductsRepoImpl @Inject constructor(private val productsDao: ProductsDao) :
    ExpiredProductsRepo {

    override fun getExpiredProducts() = productsDao.getExpiredProducts()

}