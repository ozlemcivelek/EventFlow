package com.example.eventflow.mapper

import com.example.eventflow.models.EventDetailModel
import com.example.eventflow.models.EventModel

fun EventDetailModel.toEvent() = EventModel(
    eventId = id,
    title = title,
    description = description,
    category = category,
    date = date,
    startTime = startTime,
    endTime = endTime,
    location = location,
    customerRef = customerRef,
    serviceList = serviceList,
)

fun asd() {

}

fun String.extensionTest() {
    println(this)
}

fun Int.test() {

}


fun main() {
    asd()

    "asd".extensionTest()


    112.test()

}