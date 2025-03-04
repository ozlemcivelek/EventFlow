package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : EventRepository {
    private val currentUserId: String by lazy {
        firebaseAuth.currentUser?.uid ?: ""
    }
    private val db = FirebaseFirestore.getInstance().collection("events").document("userId")
        .collection(currentUserId)

    override suspend fun getAllEvents(): List<EventModel> {
        val result = db.get().await()
        val events = result.mapNotNull {
            val event = it.toObject(EventModel::class.java)
            event.eventId = it.id
            event
        }
        return events
    }

    override suspend fun getEventById(eventId: String): EventModel? {
        val doc = db.document(eventId).get().await()
        return doc.toObject(EventModel::class.java).apply {
            this?.eventId = doc.id
        }
    }

    override suspend fun addEvent(event: EventModel): Boolean {
        val result = db.add(event).await()
        return result.id.isNotEmpty()
    }


    override suspend fun updateEvent(event: EventModel): Boolean {
        val id = event.eventId ?: return false
        db.document(id).set(event).await()
        return true
    }

    override suspend fun deleteEvent(event: EventModel): Boolean {
        val id = event.eventId ?: return false
        db.document(id).delete().await()
        return true

    }
}