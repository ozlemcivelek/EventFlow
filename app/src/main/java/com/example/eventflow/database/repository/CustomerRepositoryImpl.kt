package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.CustomerModel
import com.google.firebase.firestore.FirebaseFirestore

class CustomerRepositoryImpl: CustomerRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getCustomers(): List<CustomerModel> {
        val result = db.collection("customers").get().await()
        val customers = result.mapNotNull {
            val customer = it.toObject(CustomerModel::class.java)
            customer.customerRef = it.id
            customer
        }
        return customers
    }

    override suspend fun getCustomerById(customerId: String): CustomerModel? {
        val doc = db.collection("customers").document(customerId).get().await()
        return doc.toObject(CustomerModel::class.java)
    }

    override suspend fun addCustomer(customer: CustomerModel): String {
        val result = db.collection("customers").add(customer).await()
        return result.id
    }

    override suspend fun updateCustomer(
        customerId: String,
        customer: CustomerModel
    ): Boolean {
        db.collection("customers").document(customerId).set(customer).await()
        return true
    }

    override suspend fun deleteCustomer(customerId: String): Boolean {
        db.collection("customers").document(customerId).delete().await()
        return true
    }
}