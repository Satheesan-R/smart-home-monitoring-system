package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Device
import com.example.smarthome.data.model.SwitchItem
import com.example.smarthome.data.repository.DeviceRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DeviceDetailsViewModel : ViewModel() {
    private val repository = DeviceRepository()
    private val firestore = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null

    var device by mutableStateOf<Device?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadDevice(deviceId: String) {
        isLoading = true
        errorMessage = null
        
        listener?.remove()
        listener = firestore.collection("devices").document(deviceId)
            .addSnapshotListener { snapshot, e ->
                isLoading = false
                if (e != null) {
                    errorMessage = e.message
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    device = snapshot.toObject(Device::class.java)?.copy(id = snapshot.id)
                }
            }
    }

    fun toggleDevice() {
        device?.let { currentDevice ->
            val newStatus = if (currentDevice.status.uppercase() == "ON") "OFF" else "ON"
            repository.updateDeviceStatus(
                deviceId = currentDevice.id,
                status = newStatus,
                onSuccess = {},
                onError = { errorMessage = it.message }
            )
        }
    }

    fun toggleSwitch(switchItem: SwitchItem) {
        device?.let { currentDevice ->
            val updatedSwitches = currentDevice.switches.map {
                if (it.id == switchItem.id) {
                    it.copy(status = if (it.status.uppercase() == "ON") "OFF" else "ON")
                } else {
                    it
                }
            }
            repository.updateMultiSwitchStatus(
                deviceId = currentDevice.id,
                switches = updatedSwitches,
                onSuccess = {},
                onError = { errorMessage = it.message }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}
