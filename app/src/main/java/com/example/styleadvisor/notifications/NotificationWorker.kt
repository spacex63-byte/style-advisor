package com.example.styleadvisor.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.styleadvisor.MainActivity
import com.example.styleadvisor.R
import com.example.styleadvisor.data.NotificationModel
import com.example.styleadvisor.data.NotificationRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val timeOfDay = inputData.getString(KEY_TIME_OF_DAY) ?: "morning"
        
        val title = when (timeOfDay) {
            "morning" -> morningTitles.random()
            "evening" -> eveningTitles.random()
            "night" -> nightTitles.random()
            else -> "Style Check!"
        }
        
        val message = when (timeOfDay) {
            "morning" -> morningMessages.random()
            "evening" -> eveningMessages.random()
            "night" -> nightMessages.random()
            else -> "It's time to check out some new styles."
        }
        
        NotificationRepository.init(context)
        NotificationRepository.addNotification(
            NotificationModel(
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = "STYLE_CHECK",
                isUnread = true
            )
        )
        
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
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val notificationId = Random.nextInt()
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val dismissIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_DISMISS
            putExtra("NOTIFICATION_ID", notificationId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_shirt)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Permanently in notification panel
            .setAutoCancel(false)
            .addAction(0, "Dismiss", dismissPendingIntent)
            .addAction(0, "Check it out", pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val KEY_TIME_OF_DAY = "time_of_day"

        private val morningTitles = listOf("Morning Style Check ☀️", "Rise & Shine ☀️", "Fresh Start ☀️")
        private val morningMessages = listOf("Time to update your wardrobe for the day! Start fresh with a great outfit.", "Plan your look for a productive day.", "Morning! Let's pick out something amazing to wear.")
        
        private val eveningTitles = listOf("Evening Elegance 🌆", "Twilight Trends 🌆", "Dinner Date Ready? 🌆")
        private val eveningMessages = listOf("Heading out? Discover the perfect evening look.", "Time to switch up your style for the evening.", "Check out color harmony tips for tonight.")
        
        private val nightTitles = listOf("Nightly Style Score 🌙", "Style Wrap-up 🌙", "Tomorrow's Trends 🌙")
        private val nightMessages = listOf("Review today's style score and plan your winning outfit for tomorrow!", "Get ready for tomorrow's fashion success.", "See how your style matched up today.")

        private fun calculateInitialDelay(hourOfDay: Int): Long {
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (target.before(now)) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis - now.timeInMillis
        }

        fun scheduleDailyNotifications(context: Context) {
            val workManager = WorkManager.getInstance(context)
            
            // Notification 1: Morning (9 AM)
            val request1 = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(calculateInitialDelay(9), TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(KEY_TIME_OF_DAY to "morning"))
                .build()

            // Notification 2: Evening (6 PM)
            val request2 = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(calculateInitialDelay(18), TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(KEY_TIME_OF_DAY to "evening"))
                .build()

            // Notification 3: Night (9 PM)
            val request3 = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(calculateInitialDelay(21), TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(KEY_TIME_OF_DAY to "night"))
                .build()

            // Enqueue all requests (using replace to avoid duplicates if called multiple times)
            workManager.enqueueUniquePeriodicWork("MorningNotification", androidx.work.ExistingPeriodicWorkPolicy.UPDATE, request1)
            workManager.enqueueUniquePeriodicWork("EveningNotification", androidx.work.ExistingPeriodicWorkPolicy.UPDATE, request2)
            workManager.enqueueUniquePeriodicWork("NightNotification", androidx.work.ExistingPeriodicWorkPolicy.UPDATE, request3)
        }
    }
}
