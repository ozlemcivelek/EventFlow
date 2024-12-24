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