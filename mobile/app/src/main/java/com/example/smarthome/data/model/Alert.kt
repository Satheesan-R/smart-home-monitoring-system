package com.example.smarthome.data.model

data class Alert(
    val id: String = "",
    val deviceId: String = "",
    val type: String = "",
    val message: String = "",
    val read: Boolean = false
)
