package com.example.smarthome.data.model

data class UsageRecord(
    val id: String = "",
    val deviceId: String = "",
    val deviceName: String = "",
    val deviceType: String = "",
    val date: String = "",
    val totalSeconds: Long = 0,
    val sessions: Int = 0
)
