package com.example.task.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.task.presentation.utils.Constants.HOURLY_EXPIRY_DATE_REPORTER_REPEAT_INTERVAL
import com.example.task.presentation.utils.Constants.HOURLY_EXPIRY_DATE_REPORTER_REPEAT_TIME_UNITE
import javax.inject.Inject

class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val work =
                PeriodicWorkRequestBuilder<HourlyExpiredDateReportWorker>(
                    HOURLY_EXPIRY_DATE_REPORTER_REPEAT_INTERVAL,
                    HOURLY_EXPIRY_DATE_REPORTER_REPEAT_TIME_UNITE
                )
                    .build()
            workManager.enqueue(work)
        }
    }
}