package com.example.smarthome.data.firebase

import com.example.smarthome.data.model.Device
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSeeder {
    fun seedData() {
        val firestore = FirebaseFirestore.getInstance()
        
        firestore.collection("rooms").get().addOnSuccessListener { roomsSnapshot ->
            val livingRoomId = roomsSnapshot.documents.find { it.getString("name")?.lowercase() == "living room" }?.id
            val studyRoomId = roomsSnapshot.documents.find { it.getString("name")?.lowercase() == "study room" }?.id

            if (livingRoomId != null && studyRoomId != null) {
                val devices = listOf(
                    Device(
                        name = "Living Room Light",
                        type = "LIGHT",
                        status = "OFF",
                        roomId = livingRoomId
                    ),
                    Device(
                        name = "Living Room Switch",
                        type = "MULTI_SWITCH",
                        status = "ON",
                        roomId = livingRoomId,
                        switches = mapOf(
                            "switch1" to mapOf("name" to "Main Light", "status" to "OFF"),
                            "switch2" to mapOf("name" to "Fan", "status" to "ON"),
                            "switch3" to mapOf("name" to "Lamp", "status" to "OFF")
                        )
                    ),
                    Device(
                        name = "Living Room Camera",
                        type = "CAMERA",
                        status = "ON",
                        roomId = livingRoomId
                    ),
                    Device(
                        name = "Study Room Switch",
                        type = "MULTI_SWITCH",
                        status = "OFF",
                        roomId = studyRoomId,
                        switches = mapOf(
                            "switch1" to mapOf("name" to "Desk Lamp", "status" to "OFF"),
                            "switch2" to mapOf("name" to "Computer", "status" to "OFF")
                        )
                    )
                )

                devices.forEach { device ->
                    firestore.collection("devices")
                        .whereEqualTo("name", device.name)
                        .whereEqualTo("roomId", device.roomId)
                        .get()
                        .addOnSuccessListener { existing ->
                            if (existing.isEmpty) {
                                firestore.collection("devices").add(device)
                            }
                        }
                }
            }
        }
    }
}
