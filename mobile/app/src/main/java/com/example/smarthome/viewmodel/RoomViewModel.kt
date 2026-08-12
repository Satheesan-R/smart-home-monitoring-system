package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Device
import com.example.smarthome.data.model.Room
import com.example.smarthome.data.repository.DeviceRepository
import com.example.smarthome.data.repository.RoomRepository

class RoomViewModel : ViewModel() {

    private val roomRepository = RoomRepository()
    private val deviceRepository = DeviceRepository()

    var rooms by mutableStateOf<List<Room>>(emptyList())
        private set

    var devices by mutableStateOf<List<Device>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadRooms(floorId: String) {
        isLoading = true
        errorMessage = null

        roomRepository.getRoomsByFloor(
            floorId = floorId,
            onSuccess = { result ->
                rooms = result
                isLoading = false
            },
            onError = { exception ->
                errorMessage = exception.message
                isLoading = false
            }
        )
    }

    fun observeDevices(roomId: String) {
        isLoading = true
        errorMessage = null

        deviceRepository.observeDevicesByRoom(
            roomId = roomId,
            onUpdate = { result ->
                devices = result
                isLoading = false
            },
            onError = { exception ->
                errorMessage = exception.message
                isLoading = false
            }
        )
    }

    fun toggleDevice(device: Device) {
        val newStatus = if (device.status.uppercase() == "ON") "OFF" else "ON"

        deviceRepository.updateDeviceStatus(
            deviceId = device.id,
            status = newStatus,
            onSuccess = {
                // Real-time listener will update the UI
            },
            onError = { exception ->
                errorMessage = exception.message
            }
        )
    }
}
