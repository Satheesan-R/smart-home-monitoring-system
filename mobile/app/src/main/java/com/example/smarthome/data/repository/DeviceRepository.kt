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
                    try {
                        document.toObject(Device::class.java)
                            ?.copy(id = document.id)
                    } catch (e: Exception) {
                        null
                    }
                }
                onSuccess(devices)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun observeDevices(
        onUpdate: (List<Device>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    onError(exception)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val devices = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(Device::class.java)
                                ?.copy(id = document.id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    onUpdate(devices)
                }
            }
    }

    fun getDevicesByRoom(
        roomId: String,
        onSuccess: (List<Device>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices")
            .whereEqualTo("roomId", roomId)
            .get()
            .addOnSuccessListener { result ->
                val devices = result.documents.mapNotNull { document ->
                    try {
                        document.toObject(Device::class.java)
                            ?.copy(id = document.id)
                    } catch (e: Exception) {
                        null
                    }
                }
                onSuccess(devices)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun observeDevicesByRoom(
        roomId: String,
        onUpdate: (List<Device>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        println("REPOSITORY: Observing devices for room: $roomId")
        firestore.collection("devices")
            .whereEqualTo("roomId", roomId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("REPOSITORY: Error observing devices: ${exception.message}")
                    onError(exception)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    println("REPOSITORY: Snapshot found with ${snapshot.size()} documents")
                    val devices = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(Device::class.java)
                                ?.copy(id = document.id)
                        } catch (e: Exception) {
                            println("REPOSITORY: Deserialization error for doc ${document.id}: ${e.message}")
                            null
                        }
                    }
                    println("REPOSITORY: Successfully mapped ${devices.size} devices")
                    onUpdate(devices)
                }
            }
    }

    fun updateDeviceStatus(
        deviceId: String, 
        status: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val updates = mutableMapOf<String, Any?>(
            "status" to status
        )
        
        if (status.uppercase() == "ON") {
            updates["turnedOnAt"] = com.google.firebase.firestore.FieldValue.serverTimestamp()
        } else {
            updates["turnedOnAt"] = null
        }

        firestore.collection("devices").document(deviceId)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateMultiSwitchStatus(
        deviceId: String,
        switches: List<com.example.smarthome.data.model.SwitchItem>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("devices").document(deviceId)
            .update("switches", switches)
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
