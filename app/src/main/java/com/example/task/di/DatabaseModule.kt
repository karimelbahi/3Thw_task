package com.example.task.di

import android.content.Context
import androidx.room.Room
import com.example.myutils.data.rom.AppDatabase
import com.example.task.data.database.model.room.ProductsDao
import com.example.task.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, Constants.LOCAL_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAppDao(appDatabase: AppDatabase): ProductsDao {
        return appDatabase.appDao()
    }
}