package com.example.eventflow.models

data class ServiceModel(
    var serviceId: String? = null,
    val serviceName: String,
    val serviceDescription: String,
    val servicePrice: String,
    val serviceImage: List<String>
){
    constructor() : this("","", "", "", emptyList())
}
