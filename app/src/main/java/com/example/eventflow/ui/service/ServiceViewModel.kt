package com.example.eventflow.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.repository.ServiceRepository
import com.example.eventflow.models.ServiceModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : BaseViewModel() {

    val serviceModel: MutableLiveData<List<ServiceModel>> = MutableLiveData()
    var service: ServiceModel = ServiceModel()

    var emptyState: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _selectedItem = MutableLiveData<ServiceModel?>(null)
    val selectedItem: LiveData<ServiceModel?> get() = _selectedItem

    fun selectItem(service: ServiceModel?) {
        _selectedItem.value = service
    }

    fun isDataValid(): Boolean {
        return service.serviceName.isNotEmpty() &&
                service.serviceDescription.isNotEmpty() &&
                service.servicePrice.isNotEmpty()
    }

    fun setTitle(name: String) {
        service = service.copy(serviceName = name)
    }

    fun setDescription(description: String) {
        service = service.copy(serviceDescription = description)
    }

    fun setPrice(price: String) {
        service = service.copy(servicePrice = price)
    }

    fun saveServices() {
        sendRequest(
            call = {
                serviceRepository.addService(service)
            }, result = {
            }
        )
    }

    fun getServices() {
        sendRequest(
            call = {
                serviceRepository.getServices()
            }, result = {
                serviceModel.value = it
                emptyState.value = it.isEmpty()
            }
        )
    }

    fun deleteService(serviceId: String) {
        sendRequest(
            call = {
                serviceRepository.deleteService(serviceId)
            }, result = {
            }
        )
    }

    fun updateService(updatedService: ServiceModel) {
        val serviceId = _selectedItem.value?.serviceId ?: ""
        sendRequest(
            call = {
                serviceRepository.updateService(serviceId,updatedService)
            }, result = {
            }
        )
    }
}