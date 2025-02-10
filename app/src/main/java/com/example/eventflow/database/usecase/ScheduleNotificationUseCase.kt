package com.example.eventflow.database.usecase

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.eventflow.models.EventModel
import com.example.eventflow.ui.notification.NotificationHelper
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) {

    fun scheduleNotification(event: EventModel) {
        if (event.reminder != true) return

        val reminderTime = sharedPreferences.getInt("reminder_time", 60) // Varsayılan 1 saat önce
        Log.d("TAG", "reminderTime: $reminderTime")
        val eventTime = parseEventTime(event.date, event.startTime)
        Log.d("TAG", "eventTime: $eventTime")
        val notificationTime = eventTime - (reminderTime * 60 * 1000)
        Log.d("TAG", "notificationTime: $notificationTime")

        if (notificationTime > System.currentTimeMillis()) {
            Log.d("TAG", "notification will be shown")
            NotificationHelper.scheduleNotification(context, notificationTime, event)
        }
    }

    private fun parseEventTime(date: String, time: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.parse("$date $time")?.time ?: 0
    }
}

