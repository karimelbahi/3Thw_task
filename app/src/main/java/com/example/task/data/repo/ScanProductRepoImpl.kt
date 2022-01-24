package com.example.task.data.repo

import com.example.task.data.database.entity.Product
import com.example.task.data.database.entity.room.ProductsDao
import com.example.task.domain.repo.ScanProductRepo
import javax.inject.Inject

class ScanProductRepoImpl @Inject constructor(private val productsDao: ProductsDao) :
    ScanProductRepo {

    override suspend fun insertProduct(product: Product) = productsDao.insertProduct(product)

}