package com.example.task.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.task.R
import com.example.task.presentation.ui.main.MainActivity
import com.example.task.presentation.utils.Constants.NOTIFICATION_CHANNEL
import com.example.task.presentation.utils.Constants.NOTIFICATION_ID
import com.example.task.presentation.utils.Constants.NOTIFICATION_NAME
import com.example.task.presentation.utils.Constants.PRODUCT_CODE
import com.example.task.presentation.utils.Constants.PRODUCT_ID
import com.example.task.presentation.utils.Constants.PRODUCT_NAME
import com.example.task.presentation.utils.Constants.PRODUCT_TYPE
import com.example.task.presentation.utils.vectorToBitmap

class ExpiredProductPushNotificationWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(
        context,
        workerParams
    ) {
    override fun doWork(): Result {

        val productId = inputData.getInt(PRODUCT_ID, 0)
        val productName = inputData.getString(PRODUCT_NAME) ?: ""
        val productCode = inputData.getString(PRODUCT_CODE) ?: ""
        val productType = inputData.getString(PRODUCT_TYPE) ?: ""

        fireNotification(productId, productName, productCode, productType)
        return Result.success()
    }


    private fun fireNotification(
        productId: Int,
        productName: String,
        productCode: String,
        productType: String
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = context.vectorToBitmap(R.drawable.ic_baseline_warning_24)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL
        )
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.ic_baseline_expired_24)
            .setContentTitle(context.getString(R.string.expired_date_warn))
            .setContentText(
                context.getString(
                    R.string.expired_date_warn_notification_content,
                    productName, productCode, productType
                )
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        context.getString(
                            R.string.expired_date_warn_notification_content,
                            productName, productCode, productType
                        )
                    )
            )
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(productId, notification.build())
    }


}