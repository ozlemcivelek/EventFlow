package com.example.eventflow.ui.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.ServiceModel
import com.google.firebase.firestore.FirebaseFirestore

class ServiceViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _services: MutableList<ServiceModel> = mutableListOf()
    val services: List<ServiceModel>
        get() = _services

    private var _serviceId: String? = null
    val serviceId: String?
        get() = _serviceId

    var service: ServiceModel = ServiceModel()

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

    fun saveServices(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("services")
            .add(service)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getServices(onSuccess: (List<ServiceModel>) -> Unit,onFailure: (Exception) -> Unit) {
        db.collection("services")
            .get()
            .addOnSuccessListener { documents ->
                val services = documents.map {
                    val service = it.toObject(ServiceModel::class.java)
                    service.serviceId = it.id
                    service
                }
                _services.clear()
                _services.addAll(services)
                onSuccess(services)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteService(serviceId: String) {
        db.collection("services").document(serviceId)
            .delete()
            .addOnSuccessListener {
                // Listedeki ilgili servisi kaldır
                val removedService = _services.find { it.serviceId == serviceId }
                if (removedService != null) {
                    _services.remove(removedService)
                }
                Log.d("Firestore", "Belge başarıyla silindi.")
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Belge silinirken hata oluştu.", exception)
            }
    }

    fun updateService(updatedService: ServiceModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val serviceId = _selectedItem.value?.serviceId ?: ""
        db.collection("services").document(serviceId)
            .set(updatedService)
            .addOnSuccessListener {
                // Listedeki ilgili servisi güncelle
                val index = _services.indexOfFirst { it.serviceId == serviceId }
                if (index != -1) {
                    _services[index] = updatedService
                }
                onSuccess()
                Log.d("Firestore", "Belge başarıyla güncellendi.")
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                Log.w("Firestore", "Belge güncellenirken hata oluştu.", exception)
            }
    }

}