package com.example.smarthome.data.firebase

import com.example.smarthome.data.model.Device
import com.example.smarthome.data.model.SwitchItem
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSeeder {
    fun seedData() {
        println("SEEDER: Starting seeding...")
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
                        switchCount = 3,
                        switches = listOf(
                            SwitchItem(id = "switch1", name = "Main Light", status = "OFF"),
                            SwitchItem(id = "switch2", name = "Fan", status = "ON"),
                            SwitchItem(id = "switch3", name = "Lamp", status = "OFF")
                        )
                    ),
                    Device(
                        name = "Living Room Camera",
                        type = "CAMERA",
                        status = "ON",
                        roomId = livingRoomId,
                        snapshotUrl = "https://picsum.photos/id/237/400/300"
                    ),
                    Device(
                        name = "study_switch_01",
                        type = "MULTI_SWITCH",
                        status = "OFF",
                        roomId = studyRoomId,
                        switchCount = 2,
                        switches = listOf(
                            SwitchItem(id = "switch_1", name = "Light", status = "OFF"),
                            SwitchItem(id = "switch_2", name = "Fan", status = "OFF")
                        )
                    ),
                    Device(
                        name = "Master Bedroom Iron",
                        type = "IRON",
                        status = "OFF",
                        roomId = livingRoomId,
                        maxOnDuration = 30
                    )
                )

                devices.forEach { device ->
                    // Use a slugified name as the document ID (e.g., "living_room_light")
                    val documentId = device.name.lowercase().replace(" ", "_")
                    
                    firestore.collection("devices").document(documentId).set(device)
                        .addOnSuccessListener { 
                            println("SEEDER: Successfully seeded ${device.name} as $documentId") 
                        }
                        .addOnFailureListener { e ->
                            println("SEEDER: Error seeding ${device.name}: ${e.message}")
                        }
                }
            } else {
                println("SEEDER: Could not find required rooms for seeding")
            }
        }.addOnFailureListener {
            println("SEEDER: Failed to fetch rooms: ${it.message}")
        }
    }
}
