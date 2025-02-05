package com.example.eventflow.database.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatterUtil {
    fun calculateRemainingTime(eventDate: String, eventStartTime: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateTime = Date()

        return try {
            val eventDateTime = dateFormat.parse("$eventDate $eventStartTime")
            if (eventDateTime == null) {
                return "Geçerli zaman hatası"
            }

            val remainingTimeInMillis = eventDateTime.time - currentDateTime.time
            if (remainingTimeInMillis < 0) return "Geçti"

            val remainingDays = remainingTimeInMillis / (1000 * 60 * 60 * 24)
            val remainingHours = (remainingTimeInMillis / (1000 * 60 * 60)) % 24
            val remainingMinutes = (remainingTimeInMillis / (1000 * 60)) % 60

            return if (remainingDays > 0) {
                "$remainingDays gün $remainingHours sa $remainingMinutes dk"
            } else if (remainingHours > 0) {
                "$remainingHours sa $remainingMinutes dk"
            } else {
                "$remainingMinutes dk"
            }

        } catch (e: Exception) {
            "Hatalı tarih formatı"
        }
    }
}