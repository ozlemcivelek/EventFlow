package com.example.eventflow.database.repository

import com.example.eventflow.models.CustomerModel

interface CustomerRepository {
    suspend fun getCustomers(): List<CustomerModel>
    suspend fun getCustomerById(customerId: String): CustomerModel?
    suspend fun addCustomer(customer: CustomerModel): String
    suspend fun updateCustomer(customerId: String, customer: CustomerModel): Boolean
    suspend fun deleteCustomer(customerId: String): Boolean

}