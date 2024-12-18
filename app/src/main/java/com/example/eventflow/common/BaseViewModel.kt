package com.example.eventflow.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    fun <T> sendRequest(call: suspend () -> T, result: (T) -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val response = call()
                result(response)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                setLoading(false)
            }
        }
    }

    fun handleException(t: Throwable) {
        t.printStackTrace()
    }

    /*
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
     */


    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    protected fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}