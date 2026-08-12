package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Device
import com.example.smarthome.data.model.Room
import com.example.smarthome.data.repository.DeviceRepository
import com.example.smarthome.data.repository.RoomRepository
import com.google.firebase.firestore.FirebaseFirestore

class FloorPlanViewModel : ViewModel() {
    private val deviceRepository = DeviceRepository()
    private val roomRepository = RoomRepository()
    private val firestore = FirebaseFirestore.getInstance()

    var devices by mutableStateOf<List<Device>>(emptyList())
        private set
    var rooms by mutableStateOf<List<Room>>(emptyList())
        private set
    var floorName by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadFloorData(floorId: String) {
        isLoading = true
        errorMessage = null

        // Fetch Floor details for the name
        firestore.collection("floors").document(floorId).get()
            .addOnSuccessListener { doc ->
                floorName = doc.getString("name") ?: "Floor Plan"
            }

        // Fetch Rooms for this floor
        roomRepository.getRoomsByFloor(
            floorId = floorId,
            onSuccess = { roomList ->
                rooms = roomList
                
                // Fetch Devices for this floor (filtering by floorId)
                firestore.collection("devices")
                    .whereEqualTo("floorId", floorId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            errorMessage = e.message
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            devices = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(Device::class.java)?.copy(id = doc.id)
                            }
                        }
                        isLoading = false
                    }
            },
            onError = { e ->
                errorMessage = e.message
                isLoading = false
            }
        )
    }

    fun toggleDevice(device: Device) {
        deviceRepository.updateDeviceStatus(
            deviceId = device.id,
            status = if (device.status.uppercase() == "ON") "OFF" else "ON",
            onSuccess = {},
            onError = { errorMessage = it.message }
        )
    }
}
