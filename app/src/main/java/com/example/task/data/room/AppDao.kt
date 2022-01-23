package com.example.task.data.room

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.data.entity.Product
import com.example.task.presentation.utils.Constants
import com.example.task.presentation.utils.Constants.PRODUCT_TABLE
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Query("SELECT * FROM PRODUCT_TABLE")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM PRODUCT_TABLE WHERE expired = 0")
    fun checkProductsExpiredDateStatus(): List<Product>

    @Query("SELECT * FROM PRODUCT_TABLE WHERE expired = 1 ORDER BY expiredDate ")
    fun getExpiredProducts(): Flow<List<Product>>

    @Query("SELECT * FROM PRODUCT_TABLE WHERE expired = 0 AND warningNotificationScheduled=0")
    fun getFreshProducts(): List<Product>

    @Query("UPDATE PRODUCT_TABLE SET expiredDate = :expiredDate WHERE id =:id")
    suspend fun updateProductExpireDate(id: Int, expiredDate: Long): Int

    @Query("UPDATE PRODUCT_TABLE SET expired = :expired WHERE id =:id")
    fun updateExpireStatus(id: Int, expired: Boolean = true): Int

    @Query("UPDATE  PRODUCT_TABLE SET warningNotificationScheduled =:scheduled  WHERE warningNotificationScheduled = 0")
    fun updateScheduledNotifications(scheduled: Boolean = true): Int
//    {
//        Log.e("karimDebug", "AppDao, updateScheduledNotifications , 39");
//        return 1
//    }


}