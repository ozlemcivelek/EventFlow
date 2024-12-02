package com.example.eventflow

data class Event(
    val title: String,
    val description: String,
    val date: String,
    val firstTime: String,
    val lastTime: String
) {
    val prettyDate: String
        get() = date.toString() // Format this date for the UI
}