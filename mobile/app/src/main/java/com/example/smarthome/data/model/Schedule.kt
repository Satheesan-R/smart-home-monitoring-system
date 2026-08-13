package com.example.smarthome.data.model

data class Schedule(
    val id: String = "",
    val deviceId: String = "",
    val enabled: Boolean = false,
    val onTime: String = "",
    val offTime: String = ""
)
