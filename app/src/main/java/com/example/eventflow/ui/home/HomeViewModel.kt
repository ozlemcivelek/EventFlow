package com.example.eventflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

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
        val dateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time).uppercase()
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
        viewModelScope.launch {
            val events = eventRepository.getAllEvents()
            eventsModel.value = events
            onEventsLoaded()
        }
        return eventsModel.value ?: emptyList()
    }
}