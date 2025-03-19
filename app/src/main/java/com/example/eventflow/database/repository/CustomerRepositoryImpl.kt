package com.example.eventflow.database.repository

import com.example.eventflow.database.utils.await
import com.example.eventflow.models.CustomerModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : CustomerRepository {
    private val currentUserId: String by lazy {
        firebaseAuth.currentUser?.uid ?: ""
    }
    private val db = FirebaseFirestore.getInstance().collection("customers").document("userId")
        .collection(currentUserId)

    override suspend fun getCustomers(): List<CustomerModel> {
        val result = db.get().await()
        val customers = result.mapNotNull {
            val customer = it.toObject(CustomerModel::class.java)
            customer.customerRef = it.id
            customer
        }
        return customers
    }

    override suspend fun getCustomerById(customerId: String): CustomerModel? {
        val doc = db.document(customerId)
            .get().await()
        return doc.toObject(CustomerModel::class.java).apply {
            this?.customerRef = customerId
        }
    }

    override suspend fun addCustomer(customer: CustomerModel): String {
        val result = db.add(customer).await()
        return result.id
    }

    override suspend fun updateCustomer(
        customerId: String,
        customer: CustomerModel,
    ): Boolean {
        db.document(customerId).set(customer).await()
        return true
    }

    override suspend fun deleteCustomer(customerId: String): Boolean {
        db.document(customerId)
            .delete().await()
        return true
    }
}