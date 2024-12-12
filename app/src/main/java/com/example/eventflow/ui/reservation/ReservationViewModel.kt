package com.example.eventflow.ui.reservation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eventflow.models.EventModel
import com.example.eventflow.models.ReservationModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReservationViewModel:ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _reservation: MutableList<ReservationModel> = mutableListOf()
    val reservation: List<ReservationModel>
        get() = _reservation

    fun getReservations(onSuccess: (List<ReservationModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { documents ->
                val reservations = mutableListOf<ReservationModel>()
                val tasks = documents.map { document ->
                    val event = document.toObject(EventModel::class.java)
                    val reservationId = document.id
                    val customerRef = event.customerRef

                    if (customerRef != null) {
                        val customerDocRef = db.collection("customers").document(customerRef)
                        customerDocRef.get().addOnSuccessListener { customerDoc ->
                            val customerName = customerDoc.getString("name") ?: "Unknown"

                            val remainingTime = calculateRemainingTime(event.date, event.startTime)
                            val reservation = ReservationModel(
                                reservationId = reservationId,
                                reservationName = "Rezervasyon Adı: ${event.title}",
                                reservationDate  = "Rezervasyon Tarihi: ${event.date}",
                                reservationTime = "Saati: ${event.startTime} - ${event.endTime}",
                                reservationCustomerName = "Müşteri Adı: $customerName",
                                remainingTime = "Kalan süre: $remainingTime" // TODO: kalan süre hesaplanacak
                            )
                            reservations.add(reservation)
                            if (reservations.size == documents.size()) {
                                // Tüm veriler toplandıktan sonra callback çağrılır
                                _reservation.clear()
                                _reservation.addAll(reservations)
                                onSuccess(reservations)
                            }
                        }.addOnFailureListener {
                            Log.e("getServices", "Error fetching customer: ${it.message}")
                        }
                    } else {
                        val reservation = ReservationModel(
                            reservationId = reservationId,
                            reservationName = event.title,
                            reservationDate = event.date,
                            reservationTime = "${event.startTime} - ${event.endTime}",
                            reservationCustomerName = "Müşteri eklenmedi",
                            remainingTime = "" // TODO: kalan süre hesaplanacak
                        )
                        reservations.add(reservation)
                        if (reservations.size == documents.size()) {
                            _reservation.clear()
                            _reservation.addAll(reservations)
                            onSuccess(reservations)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun calculateRemainingTime(eventDate: String, eventStartTime: String): String {
        // Tarih ve zaman formatı (yyyy-MM-dd HH:mm)
        val dateFormat = SimpleDateFormat("MM/dd/yyyy HH", Locale.getDefault())

        // Şu anki tarih ve saat
        val currentDateTime = Date()

        // Event tarih ve saatini Date nesnesine dönüştür
        val eventDateTime = dateFormat.parse("$eventDate $eventStartTime")

        // Eğer eventDateTime null ise, geçerli zaman döndür
        if (eventDateTime == null) {
            return "Geçerli zaman hatası"
        }

        // Kalan süreyi hesapla (ms cinsinden)
        val remainingTimeInMillis = eventDateTime.time - currentDateTime.time

        // Eğer geçmişte bir tarihse, 0 ms döndür
        if (remainingTimeInMillis < 0) return "Geçti"

        // Gün, saat, dakika hesapla
        val remainingDays = remainingTimeInMillis / (1000 * 60 * 60 * 24)
        val remainingHours = (remainingTimeInMillis / (1000 * 60 * 60)) % 24
        val remainingMinutes = (remainingTimeInMillis / (1000 * 60)) % 60

        // Kalan süreyi formatla
        return if (remainingDays > 0) {
            "$remainingDays gün $remainingHours sa $remainingMinutes dk"
        } else if (remainingHours > 0) {
            "$remainingHours sa $remainingMinutes dk"
        } else {
            "$remainingMinutes dk"
        }
    }

}
