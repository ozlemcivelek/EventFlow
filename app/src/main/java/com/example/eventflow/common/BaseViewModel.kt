package com.example.eventflow.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
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

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    protected fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}