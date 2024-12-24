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
        return doc.toObject(EventModel::class.java).apply {
            this?.eventId = doc.id
        }
    }

    override suspend fun addEvent(event: EventModel): Boolean {
        val result = db.collection("events").add(event).await()
        return result.id.isNotEmpty()
    }


    override suspend fun updateEvent(event: EventModel): Boolean {
        val id = event.eventId ?: return false
        db.collection("events").document(id).set(event).await()
        return true
    }

    override suspend fun deleteEvent(eventId: String): Boolean {
        TODO("Not yet implemented")
    }
}