package com.example.eventflow.models

data class EventDetailModel(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val category: String,
    val serviceList: List<String>?,
    val customerRef: String?,
    val customerName: String?,
    val customerPhone: String?,
    val customerEmail: String?,
    val reminder: Boolean,
) {
    constructor() : this("", "", "", "", "", "", "", "", emptyList(), null, null, null, null, false)
}
