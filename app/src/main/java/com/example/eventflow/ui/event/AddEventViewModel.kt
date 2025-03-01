package com.example.eventflow.ui.event

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.repository.CustomerRepository
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.database.usecase.GetEventUseCase
import com.example.eventflow.database.usecase.ScheduleNotificationUseCase
import com.example.eventflow.mapper.toEvent
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.models.EventDetailModel
import com.example.eventflow.models.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val customerRepository: CustomerRepository,
    private val eventUseCase: GetEventUseCase,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase,
    private val sharedPreferences: SharedPreferences,
) : BaseViewModel() {

    val calendar by lazy {
        Calendar.getInstance()
    }

    val customerModel: MutableLiveData<List<CustomerModel>> = MutableLiveData()

    private val _eventDate = MutableLiveData<String>()
    val eventDate: LiveData<String> get() = _eventDate

    private val _selectedCustomer = MutableLiveData<CustomerModel>()
    val selectedCustomer: LiveData<CustomerModel> get() = _selectedCustomer

    private val _updateOrSaveSuccess = MutableLiveData<Boolean>()
    val updateOrSaveSuccess: LiveData<Boolean> get() = _updateOrSaveSuccess

    var event: EventModel = EventModel()
    var customer: CustomerModel = CustomerModel()

    val _reminderTime: Int = sharedPreferences.getInt("reminder_time", 0) / 60
    val reminderTime: Int
        get() = _reminderTime

    fun setEventFromEventDetail(eventDetailModel: EventDetailModel) {
        event = eventDetailModel.toEvent()
    }

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

    fun setReminder(reminder: Boolean) {
        event = event.copy(reminder = reminder)
    }

    fun addEvent() {
        sendRequest(
            call = {
                val success = eventRepository.addEvent(event)
                if (success) {
                    scheduleNotificationUseCase.scheduleNotification(event)
                }
                success
            }, result = {
                _updateOrSaveSuccess.value = it
            }
        )
    }

    fun updateReminderTime(time: Int) {
        sharedPreferences.edit().putInt("reminder_time", time).apply()
    }

    fun updateEvent() {
        sendRequest(
            call = {
                val success = eventRepository.updateEvent(event)
                if (success) {
                    scheduleNotificationUseCase.scheduleNotification(event)
                }
                success
            }, result = {
                _updateOrSaveSuccess.value = it
            }
        )
    }

    fun deleteEvent() {
        sendRequest(
            call = {
                eventRepository.deleteEvent(event)
            }, result = {

            }
        )
    }

    fun addCustomer(customer: CustomerModel) {
        sendRequest(
            call = {
                customerRepository.addCustomer(customer)
            },
            result = { refId ->
                _selectedCustomer.value = customer
                selectCustomer(customer)
                event = event.copy(customerRef = refId)
            }
        )
    }

    fun getCustomers() {
        sendRequest(
            call = {
                customerRepository.getCustomers()
            }, result = {
                customerModel.value = it
            }
        )
    }

    fun selectCustomer(customer: CustomerModel) {
        if (selectedCustomer.value == customer) return
        event = event.copy(customerRef = customer.customerRef)
        customerModel.value?.forEach {
            it.isSelected = it == customer
        }
        _selectedCustomer.value = customer
    }

    fun setEventDate(date: String) {
        _eventDate.value = date
    }
}