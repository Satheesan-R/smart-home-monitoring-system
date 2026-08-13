package com.example.smarthome.data.model

data class DeviceSchedule(
    val enabled: Boolean = false,
    val startTime: String = "18:00",
    val endTime: String = "22:00"
)
