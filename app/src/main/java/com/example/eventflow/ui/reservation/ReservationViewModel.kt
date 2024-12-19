package com.example.eventflow.ui.reservation

import androidx.lifecycle.MutableLiveData
import com.example.eventflow.common.BaseViewModel
import com.example.eventflow.database.usecase.GetReservationsUseCase
import com.example.eventflow.models.ReservationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationUseCase: GetReservationsUseCase,
) : BaseViewModel() {

    val reservationModel: MutableLiveData<List<ReservationModel>> = MutableLiveData()
    fun getReservations() {
        sendRequest(
            call = {
                reservationUseCase()
            },
            result = {
                reservationModel.value = it
            })
    }
}
