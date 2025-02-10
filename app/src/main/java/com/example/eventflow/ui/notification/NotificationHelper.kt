package com.example.eventflow.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.eventflow.models.EventModel

object NotificationHelper {

    fun scheduleNotification(context: Context, time: Long, event: EventModel) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("event_title", event.title)
            putExtra("event_description", event.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, event.eventId.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}
