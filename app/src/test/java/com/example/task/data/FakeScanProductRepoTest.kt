package com.example.task.data

import com.example.task.data.database.entity.Product
import com.example.task.domain.repo.ScanProductRepo

class FakeScanProductRepoTest :ScanProductRepo {

    override suspend fun insertProduct(product: Product): Long {
        TODO("Not yet implemented")
    }
}