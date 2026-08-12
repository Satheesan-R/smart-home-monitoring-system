package com.example.smarthome.data.model

import com.google.firebase.Timestamp

data class Device(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val status: String = "OFF",
    val floorId: String = "",
    val roomId: String = "",
    // Additional fields for specific device types
    val snapshotUrl: String? = null,
    val maxOnDuration: Any? = null,
    val turnedOnAt: Any? = null,
    val switchCount: Int = 0,
    val switches: List<SwitchItem> = emptyList(),
    val position: DevicePosition = DevicePosition(),
    val schedule: DeviceSchedule = DeviceSchedule()
) {
    val turnedOnAtTimestamp: Timestamp?
        get() = turnedOnAt as? Timestamp

    val maxOnDurationLong: Long
        get() = (maxOnDuration as? Number)?.toLong() ?: 0L
}
