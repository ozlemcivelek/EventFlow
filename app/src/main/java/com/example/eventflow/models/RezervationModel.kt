package com.example.eventflow.models

data class ReservationModel(
    var id: String,
    val name: String,
    val date: String,
    val time: String,
    val customerName: String?,
    val customerPhone: String?,
    val remainingTime: String,
)
