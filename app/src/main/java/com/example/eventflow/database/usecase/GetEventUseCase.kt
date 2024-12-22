package com.example.eventflow.database.usecase

import com.example.eventflow.database.repository.CustomerRepository
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.models.EventDetailModel
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val customerRepository: CustomerRepository,
) {
    suspend operator fun invoke(eventId: String): EventDetailModel? {
        eventRepository.getEventById(eventId)?.let { event ->
            event.customerRef?.let { customerRef ->
                val customerDoc = customerRepository.getCustomerById(customerRef)
                return EventDetailModel(
                    id = event.eventId.toString(),
                    title = event.title,
                    description = event.description,
                    date = event.date,
                    startTime = event.startTime,
                    endTime = event.endTime,
                    location = event.location,
                    category = event.category,
                    serviceList = event.serviceList,
                    customerName = customerDoc?.name,
                    customerPhone = customerDoc?.phone,
                    customerEmail = customerDoc?.email
                )
            }
        }
        return null
    }
}