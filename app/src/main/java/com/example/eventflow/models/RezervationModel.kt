package com.example.eventflow.models

data class ReservationModel(
    var reservationId: String,
    val reservationName: String,
    val reservationDate: String,
    val reservationTime: String,
    val reservationCustomerName: String,
    val remainingTime: String,
)
