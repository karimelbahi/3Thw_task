package com.example.task.presentation.app

import android.app.Application
import java.util.concurrent.TimeUnit
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.task.presentation.utils.Constants.THIRD_WAY_WORK_MANAGER
import com.example.task.reminder.HourlyExpiredDateReportWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<HourlyExpiredDateReportWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            THIRD_WAY_WORK_MANAGER,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
