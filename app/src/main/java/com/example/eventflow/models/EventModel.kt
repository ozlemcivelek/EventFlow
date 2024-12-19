package com.example.eventflow.models

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
) {
    constructor() : this("","", "", "", "", "", "", "")

    val prettyDate: String
        get() = date.toString() // Format this date for the UI
}