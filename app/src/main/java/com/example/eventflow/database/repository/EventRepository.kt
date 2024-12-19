package com.example.eventflow.database.repository

import com.example.eventflow.models.EventModel

interface EventRepository {
    suspend fun getAllEvents(): List<EventModel>
    suspend fun getEventById(eventId: String): EventModel?
    suspend fun addEvent(event: EventModel): Boolean
    suspend fun updateEvent(eventId: String, event: EventModel): Boolean
    suspend fun deleteEvent(eventId: String): Boolean
}
