package com.example.eventflow.database.repository

import com.example.eventflow.models.ServiceModel

interface ServiceRepository{
    suspend fun getServices(): List<ServiceModel>
    suspend fun addService(service: ServiceModel): Boolean
    suspend fun updateService(serviceId: String, service: ServiceModel): Boolean
    suspend fun deleteService(serviceId: String): Boolean
}