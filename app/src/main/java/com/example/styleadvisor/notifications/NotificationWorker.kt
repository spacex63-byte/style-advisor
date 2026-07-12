package com.example.styleadvisor.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.styleadvisor.MainActivity
import com.example.styleadvisor.R
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Style Update"
        val message = inputData.getString(KEY_MESSAGE) ?: "You have a new style recommendation!"
        
        showNotification(title, message)
        
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "style_advisor_notifications"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Style Advisor Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_sparkle) // using available icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"

        fun scheduleDailyNotifications(context: Context) {
            val workManager = WorkManager.getInstance(context)
            
            // Notification 1: 1 minute from now
            val request1 = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setInputData(workDataOf(
                    KEY_TITLE to "Morning Style Check",
                    KEY_MESSAGE to "Time to update your wardrobe for the day!"
                ))
                .build()

            // Notification 2: 2 minutes from now
            val request2 = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .setInputData(workDataOf(
                    KEY_TITLE to "Color Harmony Tip",
                    KEY_MESSAGE to "Did you know pastel colors are trending this week?"
                ))
                .build()

            // Notification 3: 3 minutes from now
            val request3 = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(3, TimeUnit.MINUTES)
                .setInputData(workDataOf(
                    KEY_TITLE to "Your Style Score",
                    KEY_MESSAGE to "Check out your overall look score based on recent uploads!"
                ))
                .build()

            // Enqueue all requests
            workManager.enqueue(listOf(request1, request2, request3))
        }
    }
}
