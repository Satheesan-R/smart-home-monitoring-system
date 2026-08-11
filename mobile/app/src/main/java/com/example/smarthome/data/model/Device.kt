package com.example.smarthome.data.model

data class Device(
    val id: String = "",
    val name: String = "",
    val type: DeviceType = DeviceType.LIGHT,
    val status: Boolean = false,
    val value: Float = 0f, // For things like brightness or temperature
    val room: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class DeviceType {
    LIGHT,
    THERMOSTAT,
    CAMERA,
    LOCK,
    PLUG,
    SENSOR
}
