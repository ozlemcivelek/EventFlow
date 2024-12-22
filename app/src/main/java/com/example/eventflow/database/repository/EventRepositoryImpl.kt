package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore

class EventRepositoryImpl : EventRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getAllEvents(): List<EventModel> {
        val result = db.collection("events").get().await()
        val events = result.mapNotNull {
            val event = it.toObject(EventModel::class.java)
            event.eventId = it.id
            event
        }
        return events
    }

    override suspend fun getEventById(eventId: String): EventModel? {
        val doc = db.collection("events").document(eventId).get().await()
        return doc.toObject(EventModel::class.java)
    }

    override suspend fun addEvent(event: EventModel): Boolean {
        val result = db.collection("events").add(event).await()
        return result.id.isNotEmpty()
    }


    override suspend fun updateEvent(
        eventId: String,
        event: EventModel
    ): Boolean {
        db.collection("events").document(eventId).set(event).await()
        return true
    }

    override suspend fun deleteEvent(eventId: String): Boolean {
        TODO("Not yet implemented")
    }
}