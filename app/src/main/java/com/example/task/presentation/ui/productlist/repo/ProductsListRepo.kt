package com.example.task.presentation.ui.scanproduct.repo

import android.util.Log
import com.example.task.data.entity.Product
import com.example.task.data.room.AppDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map


class ProductsListRepo(private val appDao: AppDao) {

    fun checkProductsExpiredDateStatus() {
        appDao.checkProductsExpiredDateStatus().map { product ->

            /*           products.forEachIndexed { _, product ->
                           Log.e("karimDebug","${product.id} ${product.expiredDate} ${System.currentTimeMillis()} ProductsListRepo, checkProductsExpiredDateStatus , 13");
                           if (product.expiredDate < System.currentTimeMillis()) {
                               Log.e("karimDebug","${product.id}ProductsListRepo, checkProductsExpiredDateStatus , 14");
                               appDao.updateExpireStatus(product.id)
                           }
                       }*/
//            for (product in products) {
                Log.e(
                    "karimDebug",
                    "${product.id} ${product.expiredDate} ${System.currentTimeMillis()} ProductsListRepo, checkProductsExpiredDateStatus , 13"
                );
                if (product.expiredDate < System.currentTimeMillis()) {
                    Log.e(
                        "karimDebug",
                        "${product.id}ProductsListRepo, checkProductsExpiredDateStatus , 14"
                    );
                    appDao.updateExpireStatus(product.id)
                }

//            }
        }
    }

    fun getProducts() = appDao.getProducts()
}