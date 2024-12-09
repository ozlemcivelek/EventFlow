package com.example.eventflow.ui.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class EventDetailViewModel : ViewModel() {
    // TODO: Müşteri listesi için, view model ayağa kalktığında istek atılacak ve müşteriler bir değişkene yazılacak.

    private val db = FirebaseFirestore.getInstance()
    private val calendar by lazy {
        Calendar.getInstance()
    }

    private val _customers: MutableList<CustomerModel> = mutableListOf()
    val customers: List<CustomerModel>
        get() = _customers

    private val _eventDate = MutableLiveData<String>()
    val eventDate: LiveData<String> get() = _eventDate

    private val _selectedCustomer = MutableLiveData<CustomerModel>()
    val selectedCustomer: LiveData<CustomerModel> get() = _selectedCustomer

    var event: EventModel = EventModel()

    init {
        _eventDate.value = getDateFormatted()
    }

    fun getDateFormatted(): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d/%02d/%d", day, month + 1, year)
    }

    fun isDataValid(): Boolean {
        return event.title.isNotEmpty() &&
                event.category.isNotEmpty() &&
                event.date.isNotEmpty() &&
                event.startTime.isNotEmpty() &&
                event.endTime.isNotEmpty() &&
                event.location.isNotEmpty()
    }

    fun setTitle(title: String) {
        event = event.copy(title = title)
    }

    fun setDescription(description: String) {
        event = event.copy(description = description)
    }

    fun setCategory(category: String) {
        event = event.copy(category = category)
    }

    fun setDate(date: String) {
        event = event.copy(date = date)
    }

    fun setStartTime(startTime: String) {
        event = event.copy(startTime = startTime)
    }

    fun setEndTime(endTime: String) {
        event = event.copy(endTime = endTime)
    }

    fun setLocation(location: String) {
        event = event.copy(location = location)
    }

    fun saveEvent(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .add(event)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun saveCustomers(
        customer: CustomerModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("customers")
            .add(customer)
            .addOnSuccessListener {
                _customers.add(customer)
                selectCustomer(customer)
                event = event.copy(customerRef = it.id)
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getFirebaseCustomers(
        onSuccess: (List<CustomerModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("customers")
            .get()
            .addOnSuccessListener { documents ->
                val customers = documents.map { it.toObject(CustomerModel::class.java) }
                Log.d("TAG", "customers: $customers")
                this._customers.clear()
                this._customers.addAll(customers)
                onSuccess(customers)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun selectCustomer(customer: CustomerModel) {
        if (selectedCustomer.value == customer) return
        _customers.forEach {
            it.isSelected = it == customer
        }
        _selectedCustomer.value = customer
    }

    fun setEventDate(date: String) {
        _eventDate.value = date
    }
}