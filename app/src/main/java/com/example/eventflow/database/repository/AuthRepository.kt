package com.example.eventflow.database.repository

import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun updateProfile(name: String) : Resource<Boolean>
    suspend fun changePassword(currentPassword: String, password: String) : Resource<Boolean>
    fun logout()

}