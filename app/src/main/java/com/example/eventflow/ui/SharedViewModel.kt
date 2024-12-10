package com.example.eventflow.ui

import androidx.lifecycle.ViewModel
import java.util.Calendar

class SharedViewModel : ViewModel() {
    private var _calendarTime: Long = Calendar.getInstance().timeInMillis
    val calendarTime: Long
        get() = _calendarTime

    fun setCalendarTime(time: Long) {
        _calendarTime = time
    }
}
