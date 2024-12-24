package com.example.eventflow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.EventDetailModel
import java.util.Calendar

class SharedViewModel : ViewModel() {
    private var _calendarTime: Long = Calendar.getInstance().timeInMillis
    val calendarTime: Long
        get() = _calendarTime

    fun setCalendarTime(time: Long) {
        _calendarTime = time
    }


    private val _selectedItem = MutableLiveData<EventDetailModel?>(null)
    val selectedItem: LiveData<EventDetailModel?> get() = _selectedItem

    fun selectedItem(event: EventDetailModel?) {
        _selectedItem.value = event
    }
}
