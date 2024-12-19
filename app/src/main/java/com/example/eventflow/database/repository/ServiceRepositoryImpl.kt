package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.ServiceModel
import com.google.firebase.firestore.FirebaseFirestore

class ServiceRepositoryImpl : ServiceRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getServices(): List<ServiceModel> {
        val result = db.collection("services").get().await()
        val services = result.mapNotNull {
            val service = it.toObject(ServiceModel::class.java)
            service.serviceId = it.id
            service
        }
        return services
    }

    override suspend fun addService(service: ServiceModel): Boolean {
        db.collection("services").add(service).await()
        return true
    }

    override suspend fun updateService(
        serviceId: String,
        service: ServiceModel
    ): Boolean {
        db.collection("services").document(serviceId).set(service).await()
        return true
    }

    override suspend fun deleteService(serviceId: String): Boolean {
        db.collection("services").document(serviceId).delete().await()
        return true
    }
}