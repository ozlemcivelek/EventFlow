package com.example.eventflow.ui.home

import androidx.lifecycle.ViewModel
import com.example.eventflow.Event

class HomeViewModel : ViewModel() {

    //tarihe göre fitreleme yap. 2024-12-02
    fun getEvents(): List<Event> {
        val description = "Lorem ipsum dolor sit amet, consectetur radicalising elit, sed do usermod temper incident ut labor et do lore magna aliquot. " +
                "Ut enum ad minim venial, quits nostrum excitation McCull och labors nisei ut aliquot ex ea commode consequent. " +
                "Dis auto inure dolor in reprehend in voluptuary veldt ease cilium do lore eu fugit null parturition."

        val date = "DEC\n02"
        return listOf(
            Event(
                title = "Nişan Töreni",
                description = description,
                date = date,
                firstTime = "18:00",
                lastTime = "21:00"
            ),
            Event(
                title = "Söz Töreni",
                description = description,
                date = date,
                firstTime = "17:00",
                lastTime = "20:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            ),
            Event(
                title = "Workshop",
                description = description,
                date = date,
                firstTime = "10:00",
                lastTime = "13:00"
            )
        )
    }
}