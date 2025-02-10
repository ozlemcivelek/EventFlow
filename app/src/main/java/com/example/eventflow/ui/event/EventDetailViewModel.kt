package com.example.eventflow.ui.event

import androidx.lifecycle.MutableLiveData
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.usecase.GetEventUseCase
import com.example.eventflow.models.EventDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
) : BaseViewModel() {

    val eventDetail = MutableLiveData<EventDetailModel>()

    fun getEvent(eventId: String) {
        sendRequest(
            call = {
                getEventUseCase(eventId)
            },
            result = {
                eventDetail.value = it
            }
        )
    }
}