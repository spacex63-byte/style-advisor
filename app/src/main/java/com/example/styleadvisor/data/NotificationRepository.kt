package com.example.styleadvisor.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NotificationModel(
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: String,
    val isUnread: Boolean = true
)

object NotificationRepository {
    private val _notifications = MutableStateFlow<List<NotificationModel>>(emptyList())
    val notifications: StateFlow<List<NotificationModel>> = _notifications.asStateFlow()
    
    private var sharedPreferences: SharedPreferences? = null
    private const val PREFS_NAME = "StyleAdvisorNotifications"
    private const val KEY_NOTIFICATIONS = "notification_items"
    
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadNotifications()
        }
    }
    
    private fun loadNotifications() {
        val jsonString = sharedPreferences?.getString(KEY_NOTIFICATIONS, null)
        if (jsonString != null) {
            try {
                val items = Json.decodeFromString<List<NotificationModel>>(jsonString)
                _notifications.value = items
            } catch (e: Exception) {
                _notifications.value = emptyList()
            }
        } else {
            // Default sample notifications if empty
            val initial = listOf(
                NotificationModel("Welcome to Style Advisor", "We're excited to help you find your best looks. Upload an outfit to get started!", System.currentTimeMillis() - 86400000 * 2, "SCORE", false)
            )
            _notifications.value = initial
            saveNotifications(initial)
        }
    }
    
    fun addNotification(notification: NotificationModel) {
        val currentList = _notifications.value.toMutableList()
        currentList.add(0, notification) // Add to top
        // Keep max 50 notifications
        val trimmedList = if (currentList.size > 50) currentList.take(50) else currentList
        _notifications.value = trimmedList
        saveNotifications(trimmedList)
    }
    
    fun markAllAsRead() {
        val currentList = _notifications.value.map { it.copy(isUnread = false) }
        _notifications.value = currentList
        saveNotifications(currentList)
    }
    
    private fun saveNotifications(items: List<NotificationModel>) {
        try {
            val jsonString = Json.encodeToString(items)
            sharedPreferences?.edit()?.putString(KEY_NOTIFICATIONS, jsonString)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
