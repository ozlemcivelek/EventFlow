package com.example.eventflow.models

data class ReservationModel(
    var id: String,
    val name: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val customerName: String?,
    val customerPhone: String?,
    val remainingTime: String,
    val reminder: Boolean,
) {
    val time = "$startTime - $endTime"
}
