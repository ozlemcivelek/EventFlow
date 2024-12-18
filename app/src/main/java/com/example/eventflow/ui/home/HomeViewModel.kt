package com.example.eventflow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.models.EventModel
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
) : BaseViewModel() {

    private val calendar by lazy {
        Calendar.getInstance()
    }

    val eventsModel: MutableLiveData<List<EventModel>> = MutableLiveData()
    private val originalList: MutableList<EventModel> = mutableListOf()

    private val _filteredEvents = MutableLiveData<List<EventModel>>()
    val filteredEvents: LiveData<List<EventModel>> get() = _filteredEvents

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
    }

    private fun formatPrettyDate(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMM\ndd", Locale.ENGLISH)
        val parsedDate = inputFormat.parse(date)
        return parsedDate?.let { outputFormat.format(it).uppercase() } ?: date
    }

    fun getEvents() { //Update list
        viewModelScope.launch {
            setLoading(true)
            try {
                val events = eventRepository.getAllEvents()
                originalList.clear()
                originalList.addAll(events)
                eventsModel.value = events
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading events", e)
            } finally {
                setLoading(false)
            }
        }
    }

    fun getEvents2() {
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