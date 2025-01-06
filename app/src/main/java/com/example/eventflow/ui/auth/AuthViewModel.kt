package com.example.eventflow.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventflow.database.repository.AuthRepository
import com.example.eventflow.database.repository.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signupFlow: LiveData<Resource<FirebaseUser>?> = _signupFlow

    private val _updateFlow = MutableLiveData<Resource<Boolean>?>(null)
    val updateFlow: LiveData<Resource<Boolean>?> = _updateFlow

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null) _loginFlow.value = Resource.Success(repository.currentUser!!)
    }


    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result

    }

    fun signUp(name:String, email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signUp(name, email, password)
        _signupFlow.value = result
    }

    fun updateProfile(name: String) = viewModelScope.launch {
        _updateFlow.value = Resource.Loading
        val result = repository.updateProfile(name)
        _updateFlow.value = result
    }

    fun logout() {
        repository.logout()
        _logoutStatus.value = true
        _loginFlow.value = null
        _signupFlow.value = null
        _updateFlow.value = null
    }

}