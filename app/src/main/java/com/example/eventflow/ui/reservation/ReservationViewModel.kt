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

    var upcomingReservations = listOf<ReservationModel>()
    var pastReservations = listOf<ReservationModel>()

    val tabPosition: MutableLiveData<Int> = MutableLiveData()

    fun getReservations() {
        sendRequest(
            call = {
                reservationUseCase()
            },
            result = { allReservations ->
                val upcoming = allReservations.filter { it.remainingTime != "Geçti" }
                val past = allReservations.filter { it.remainingTime == "Geçti" }

                upcomingReservations = upcoming
                pastReservations = past
                tabPosition.value = 0
            })
    }
}
