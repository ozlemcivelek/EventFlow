package com.example.eventflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val calendar by lazy {
        Calendar.getInstance()
    }

    // LiveData for filtered event list
    private val _filteredEvents = MutableLiveData<List<Event>>()
    val filteredEvents: LiveData<List<Event>> get() = _filteredEvents

    // Filter events by date
    fun filterEventsByDate(time: Long) {
        val formattedDate = getFormattedDay(time)
        val filtered = getEvents().filter { it.date == formattedDate }
        _filteredEvents.value = filtered
    }

    private fun getFormattedDay(time: Long): String {
        calendar.timeInMillis = time
        val initialMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time).uppercase()
        val initialDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        return "$initialMonth\n$initialDay"
    }

    private fun getEvents(): List<Event> { //Update list
        val description =
            "Lorem ipsum dolor sit amet, consectetur radicalising elit, sed do usermod temper incident ut labor et do lore magna aliquot. " +
                    "Ut enum ad minim venial, quits nostrum excitation McCull och labors nisei ut aliquot ex ea commode consequent. " +
                    "Dis auto inure dolor in reprehend in voluptuary veldt ease cilium do lore eu fugit null parturition."

        val date = "DEC\n02"
        return listOf(
            Event(
                title = "Nişan Töreni",
                description = description,
                date = "DEC\n02",
                firstTime = "18:00",
                lastTime = "21:00"
            ),
            Event(
                title = "Söz Töreni",
                description = description,
                date = "DEC\n03",
                firstTime = "17:00",
                lastTime = "20:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n13",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n02",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n02",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n13",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n03",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n14",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n15",
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = "DEC\n",
                firstTime = "10:00",
                lastTime = "13:00"
            )
        )
    }
}