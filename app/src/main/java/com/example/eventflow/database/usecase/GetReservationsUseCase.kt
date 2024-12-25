package com.example.eventflow.database.usecase

import com.example.eventflow.database.repository.CustomerRepository
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.database.utils.DateFormatterUtil
import com.example.eventflow.models.ReservationModel
import javax.inject.Inject

class GetReservationsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val customerRepository: CustomerRepository,
) {
    suspend operator fun invoke(): List<ReservationModel> {
        val events = eventRepository.getAllEvents()

        val reservations = mutableListOf<ReservationModel>()
        for (event in events) {
            var customerName: String? = null
            var customerPhone: String? = null

            val customerRef = event.customerRef
            if (!customerRef.isNullOrEmpty()) {
                val customerDoc = customerRepository.getCustomerById(customerRef)
                customerName = customerDoc?.name
                customerPhone = customerDoc?.phone
            }

            val remainingTime = DateFormatterUtil.calculateRemainingTime(event.date, event.startTime)
            val reservation = ReservationModel(
                id = event.customerRef ?: "",
                name = event.title,
                date = event.date,
                time = "${event.startTime} - ${event.endTime}",
                customerName = customerName,
                customerPhone = customerPhone,
                remainingTime = remainingTime
            )
            reservations.add(reservation)
        }
        return reservations
    }
}