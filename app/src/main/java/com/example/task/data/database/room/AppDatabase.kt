package com.example.myutils.data.rom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task.data.database.entity.Product
import com.example.task.data.database.entity.room.ProductsDao

@Database(entities = [Product::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): ProductsDao

}