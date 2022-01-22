package com.example.task.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.data.entity.Product
import com.example.task.presentation.utils.Constants
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Query("SELECT * FROM ${Constants.PRODUCT_TABLE}")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM ${Constants.PRODUCT_TABLE} WHERE expired = 1 ORDER BY expired ")
    fun getExpiredProducts(): Flow<List<Product>>


}