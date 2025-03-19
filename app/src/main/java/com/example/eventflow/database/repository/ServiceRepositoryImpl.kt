package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.ServiceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ServiceRepository {
    private val currentUserId: String by lazy {
        firebaseAuth.currentUser?.uid ?: ""
    }
    private val db = FirebaseFirestore.getInstance().collection("services").document("userId")
        .collection(currentUserId)

    override suspend fun getServices(): List<ServiceModel> {
        val result = db.get().await()
        val services = result.mapNotNull {
            val service = it.toObject(ServiceModel::class.java)
            service.serviceId = it.id
            service
        }
        return services
    }

    override suspend fun addService(service: ServiceModel): Boolean {
        db.add(service).await()
        return true
    }

    override suspend fun updateService(
        serviceId: String,
        service: ServiceModel,
    ): Boolean {
        db.document(serviceId).set(service).await()
        return true
    }

    override suspend fun deleteService(serviceId: String): Boolean {
        db.document(serviceId).delete().await()
        return true
    }
}