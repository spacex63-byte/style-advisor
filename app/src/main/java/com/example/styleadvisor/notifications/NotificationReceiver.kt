package com.example.styleadvisor.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", -1)
        
        if (action == ACTION_DISMISS && notificationId != -1) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }

    companion object {
        const val ACTION_DISMISS = "com.example.styleadvisor.ACTION_DISMISS_NOTIFICATION"
    }
}
