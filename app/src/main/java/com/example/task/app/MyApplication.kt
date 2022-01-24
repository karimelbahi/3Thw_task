package com.example.task.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.task.presentation.utils.Constants.HOURLY_EXPIRY_DATE_REPORTER_REPEAT_INTERVAL
import com.example.task.presentation.utils.Constants.HOURLY_EXPIRY_DATE_REPORTER_REPEAT_TIME_UNITE
import com.example.task.presentation.utils.Constants.THIRD_WAY_WORK_MANAGER
import com.example.task.reminder.HourlyExpiredDateReportWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }


    }


    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val context: Context = MyApplication.applicationContext()


        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<HourlyExpiredDateReportWorker>(
                HOURLY_EXPIRY_DATE_REPORTER_REPEAT_INTERVAL,
                HOURLY_EXPIRY_DATE_REPORTER_REPEAT_TIME_UNITE
            ).build()
        workManager.enqueueUniquePeriodicWork(
            THIRD_WAY_WORK_MANAGER,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
