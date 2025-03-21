package com.example.eventflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : BaseViewModel() {

    private val calendar by lazy {
        Calendar.getInstance()
    }

    val eventsModel: MutableLiveData<List<EventModel>> = MutableLiveData()
    private val originalList: MutableList<EventModel> = mutableListOf()

    private val _filteredEvents = MutableLiveData<List<EventModel>>()
    val filteredEvents: LiveData<List<EventModel>> get() = _filteredEvents

    var emptyState: MutableLiveData<Boolean> = MutableLiveData(false)

    // Filter events by date
    fun filterEventsByDate(time: Long) {
        calendar.timeInMillis = time
        val dateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time).uppercase()
        val filtered = originalList.filter { it.prettyDate == dateFormat }
        val updatedEvents = filtered.map { event ->
            event.copy(date = formatPrettyDate(event.prettyDate))
        }
        _filteredEvents.value = updatedEvents
        emptyState.value = updatedEvents.isEmpty()
    }

    private fun formatPrettyDate(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMM\ndd", Locale.ENGLISH)
        val parsedDate = inputFormat.parse(date)
        return parsedDate?.let { outputFormat.format(it).uppercase() } ?: date
    }

    fun getEvents() {
        sendRequest(
            call = {
                eventRepository.getAllEvents()
            },
            result = {
                originalList.clear()
                originalList.addAll(it)
                eventsModel.value = it
            }
        )
    }
}