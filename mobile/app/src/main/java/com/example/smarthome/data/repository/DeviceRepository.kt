package com.example.smarthome.data.repository

import com.example.smarthome.data.model.Device
import com.google.firebase.firestore.FirebaseFirestore

class DeviceRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getDevices(
        onSuccess: (List<Device>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .get()
            .addOnSuccessListener { result ->
                val devices = result.documents.mapNotNull { document ->
                    document.toObject(Device::class.java)
                        ?.copy(id = document.id)
                }
                onSuccess(devices)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun updateDeviceStatus(
        deviceId: String, 
        status: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices").document(deviceId)
            .update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun addDevice(
        device: Device,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .add(device)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { onError(it) }
    }

    fun deleteDevice(
        deviceId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices").document(deviceId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}
