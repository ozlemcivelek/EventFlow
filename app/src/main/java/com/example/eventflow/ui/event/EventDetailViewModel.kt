package com.example.eventflow.ui.event

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.Customer
import com.example.eventflow.models.Event
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var event: Event = Event()

    fun isDataValid(): Boolean {
        return event.title.isNotEmpty() &&
        event.description.isNotEmpty() &&
        event.category.isNotEmpty() &&
        event.date.isNotEmpty() &&
        event.startTime.isNotEmpty() &&
        event.endTime.isNotEmpty() &&
        event.location.isNotEmpty()
    }

    fun setTitle(title: String) {
        event = event.copy(title = title)
    }

    fun setDescription(description: String) {
        event = event.copy(description = description)
    }

    fun setCategory(category: String) {
        event = event.copy(category = category)
    }

    fun setDate(date: String) {
        event = event.copy(date = date)
    }

    fun setStartTime(startTime: String) {
        event = event.copy(startTime = startTime)
    }

    fun setEndTime(endTime: String) {
        event = event.copy(endTime = endTime)
    }

    fun setLocation(location: String) {
        event = event.copy(location = location)
    }

    fun saveEvent(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .add(event)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun saveCustomers(customers: Customer, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("customers")
            .add(customers)
            .addOnSuccessListener {
                event = event.copy(customerRef = it.id)
                Log.d("TAG", "customer ref: ${it.id}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val events = documents.map { it.toObject(Event::class.java) }
                onSuccess(events)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}