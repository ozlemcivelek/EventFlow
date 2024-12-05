package com.example.eventflow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.String

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val calendar by lazy {
        Calendar.getInstance()
    }

    private val eventsModel: MutableLiveData<List<EventModel>> = MutableLiveData(emptyList())

    private val _filteredEvents = MutableLiveData<List<EventModel>>()
    val filteredEvents: LiveData<List<EventModel>> get() = _filteredEvents

    // Filter events by date
    fun filterEventsByDate(time: Long) {
        calendar.timeInMillis = time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time).uppercase()
        val filtered = getEvents().filter { it.prettyDate == dateFormat }
        val updatedEvents = filtered.map { event ->
            event.copy(date = formatPrettyDate(event.prettyDate))
        }
        _filteredEvents.value = updatedEvents
    }

    private fun formatPrettyDate(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMM\ndd", Locale.ENGLISH)
        val parsedDate = inputFormat.parse(date)
        return parsedDate?.let { outputFormat.format(it).uppercase() } ?: date
    }

    fun getEvents(onEventsLoaded: () -> Unit = {}): List<EventModel> { //Update list
        getFirebaseEvents(
            onSuccess = { events ->
                events.map { it ->
                     EventModel(
                        title = it.title,
                        description = it.description,
                        category = it.category,
                        date = it.prettyDate,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        location = it.location,
                        customerRef = it.customerRef
                    )
                }
                eventsModel.value = events
                onEventsLoaded() //veriler yüklendikten sonra işlem yapılacak
                Log.d("EventFilter", "Event Filtrelenmemiş: $events")
            },
            onFailure = { exception ->
                // Hata durumunda bir işlem yapabilirsiniz, örn: Log veya hata mesajı gösterimi
                Log.e("EventFilter", "Error fetching events: ${exception.message}")
            }
        )

        return eventsModel.value ?: emptyList()
    }

    private fun getFirebaseEvents(onSuccess: (List<EventModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val events = documents.mapNotNull { it.toObject(EventModel::class.java) }
                onSuccess(events)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}