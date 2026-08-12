package com.example.smarthome.data.model

data class Device(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val status: String = "",
    val floorId: String = "",
    val roomId: String = "",
    // Additional fields for specific device types
    val snapshotUrl: String? = null,
    val maxOnDuration: Any? = null,
    val turnedOnAt: Any? = null,
    val switches: Map<String, Map<String, String>>? = null
)
