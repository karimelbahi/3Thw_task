package com.example.task.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.task.presentation.app.MyApplication
import com.example.task.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationInstance(): MyApplication {
        return MyApplication()
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext


}