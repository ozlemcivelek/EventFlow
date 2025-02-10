package com.example.eventflow.models

import com.google.firebase.firestore.Exclude

data class EventModel(
    var eventId: String? = null,
    val title: String,
    val description: String,
    val category: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    var customerRef: String? = null,
    var serviceList: List<String>? = null,
    val reminder: Boolean = false,
) {
    constructor() : this("", "", "", "", "", "", "", "")

    val prettyDate: String
        @Exclude
        get() = date.toString() // Format this date for the UI
}