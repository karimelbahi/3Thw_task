package com.example.task.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.task.presentation.utils.Constants

@Entity(tableName = Constants.PRODUCT_TABLE)
data class Product(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "expiredDate")
    val expiredDate: Long,
    @ColumnInfo(name = "expired")
    val expired: Boolean = false,
    @ColumnInfo(name = "warningNotificationScheduled")
    val warningNotificationScheduled: Boolean = false
)