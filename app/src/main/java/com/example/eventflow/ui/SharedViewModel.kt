package com.example.eventflow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.EventModel
import java.util.Calendar

class SharedViewModel : ViewModel() {
    private var _calendarTime: Long = Calendar.getInstance().timeInMillis
    val calendarTime: Long
        get() = _calendarTime

    fun setCalendarTime(time: Long) {
        _calendarTime = time
    }


    private val _selectedItem = MutableLiveData<EventModel?>(null)
    val selectedItem: LiveData<EventModel?> get() = _selectedItem

    fun selectedItem(event: EventModel?) {
        _selectedItem.value = event
    }
}
