package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore

class EventRepositoryImpl : EventRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getAllEvents(): List<EventModel> {
        val result = db.collection("events").get().await()

        val events = result.mapNotNull { it.toObject(EventModel::class.java) }
        return events
    }

    override suspend fun getEventById(eventId: String): EventModel? {
        TODO("Not yet implemented")
    }

    override suspend fun addEvent(event: EventModel): Boolean {
        val result = db.collection("events").add(event).await()
        return result.id.isNotEmpty()
    }


    override suspend fun updateEvent(
        eventId: String,
        event: EventModel
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: String): Boolean {
        TODO("Not yet implemented")
    }
}