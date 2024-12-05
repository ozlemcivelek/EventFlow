package com.example.eventflow.ui.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class EventDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val calendar by lazy {
        Calendar.getInstance()
    }
    private val _eventDate = MutableLiveData<String>()
    val eventDate: LiveData<String> get() = _eventDate

    var event: EventModel = EventModel()

    init {
        _eventDate.value = getDateFormatted()
    }

    fun getDateFormatted() : String{
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return  String.format("%02d/%02d/%d", day, month + 1, year)
    }

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

    fun saveCustomers(customers: CustomerModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
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

    fun getEvents(onSuccess: (List<EventModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val events = documents.map { it.toObject(EventModel::class.java) }
                onSuccess(events)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun setEventDate(date: String) {
        _eventDate.value = date
    }
}