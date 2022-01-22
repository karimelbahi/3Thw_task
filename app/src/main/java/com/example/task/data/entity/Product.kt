package com.example.task.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.task.presentation.utils.Constants

@Entity(tableName = Constants.PRODUCT_TABLE)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val name: String,
    val type: String,
    val expiredDate: Long,
    val expired :Boolean=false
)