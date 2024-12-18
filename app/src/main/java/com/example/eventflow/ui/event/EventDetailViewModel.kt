package com.example.eventflow.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    // TODO: Müşteri listesi için, view model ayağa kalktığında istek atılacak ve müşteriler bir değişkene yazılacak.

    private val db = FirebaseFirestore.getInstance()
    val calendar by lazy {
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
    var customer: CustomerModel = CustomerModel()

    fun setCalendarTime(time: Long) {
        calendar.timeInMillis = time
        setEventDate(getDateFormatted())
    }

    fun getDateFormatted(): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d/%02d/%d", day, month + 1, year)
    }

    fun isDataEventValid(): Boolean {
        return event.title.isNotEmpty() &&
                event.date.isNotEmpty() &&
                event.startTime.isNotEmpty() &&
                event.endTime.isNotEmpty() &&
                event.location.isNotEmpty() &&
                selectedCustomer.value != null &&
                event.serviceList?.isNotEmpty() == true
    }

    fun isDataCustomerValid(): Boolean {
        return customer.name.isNotEmpty() &&
                customer.email.isNotEmpty() &&
                customer.phone.isNotEmpty()
    }

    fun setCustomerName(name: String) {
        customer = customer.copy(name = name)
    }

    fun setCustomerEmail(email: String) {
        customer = customer.copy(email = email)
    }

    fun setCustomerPhone(phone: String) {
        customer = customer.copy(phone = phone)
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

    fun setServices(services: List<String>) {
        event = event.copy(serviceList = services)
    }

    fun saveEvent(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            val isEventSaved = eventRepository.addEvent(event)
            if (isEventSaved) {
                onSuccess()
            } else {
                onFailure(Exception("Event could not be saved"))
            }
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
                _selectedCustomer.value = customer
                _customers.add(customer) // TODO: Burada sadece listeye eklendiği için veri tabanında customerRef değerini almıyor.
                selectCustomer(customer) // TODO: Event içindeki customerRef i bulup onun id si eklenecek.
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
                val customers = documents.map {
                    val customer = it.toObject(CustomerModel::class.java)
                    customer.customerRef = it.id
                    customer
                }
                _customers.clear()
                _customers.addAll(customers)
                onSuccess(customers)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun selectCustomer(customer: CustomerModel) {
        if (selectedCustomer.value == customer) return
        event = event.copy(customerRef = customer.customerRef)
        _customers.forEach {
            it.isSelected = it == customer
        }
        _selectedCustomer.value = customer
    }

    fun setEventDate(date: String) {
        _eventDate.value = date
    }
}