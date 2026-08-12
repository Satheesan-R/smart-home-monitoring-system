package com.example.smarthome.data.firebase

import com.example.smarthome.data.model.Device
import com.example.smarthome.data.model.DevicePosition
import com.example.smarthome.data.model.Floor
import com.example.smarthome.data.model.Room
import com.example.smarthome.data.model.SwitchItem
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSeeder {
    fun seedData() {
        println("SEEDER: Starting seeding...")
        val firestore = FirebaseFirestore.getInstance()

        // 1. Seed Floors
        val groundFloorId = "ground_floor"
        val groundFloor = Floor(name = "Ground Floor", floorNumber = 0)
        
        val firstFloorId = "first_floor"
        val firstFloor = Floor(name = "First Floor", floorNumber = 1)
        
        firestore.collection("floors").document(groundFloorId).set(groundFloor)
            .addOnSuccessListener { println("SEEDER: Seeded Floor: $groundFloorId") }
            
        firestore.collection("floors").document(firstFloorId).set(firstFloor)
            .addOnSuccessListener {
                println("SEEDER: Seeded Floor: $firstFloorId")
                
                // 2. Seed Rooms
                // Bedroom on First Floor
                val bedroomId = "bedroom"
                val bedroom = Room(name = "Bedroom", floorId = firstFloorId)
                
                firestore.collection("rooms").document(bedroomId).set(bedroom)
                    .addOnSuccessListener {
                        println("SEEDER: Seeded Room: $bedroomId")
                        
                        // Seed Devices for Bedroom
                        val bedroomDevices = listOf(
                            Device(
                                name = "Bedroom Light",
                                type = "LIGHT",
                                status = "OFF",
                                roomId = bedroomId,
                                floorId = firstFloorId,
                                position = DevicePosition(0.2f, 0.2f)
                            ),
                            Device(
                                name = "Bedroom Iron",
                                type = "IRON",
                                status = "OFF",
                                roomId = bedroomId,
                                floorId = firstFloorId,
                                maxOnDuration = 60, // 60 seconds for testing
                                position = DevicePosition(0.8f, 0.3f)
                            ),
                            Device(
                                name = "Bedroom Outlet",
                                type = "OUTLET",
                                status = "OFF",
                                roomId = bedroomId,
                                floorId = firstFloorId,
                                position = DevicePosition(0.5f, 0.8f)
                            ),
                            Device(
                                name = "Bedroom Camera",
                                type = "CAMERA",
                                status = "ON",
                                roomId = bedroomId,
                                floorId = firstFloorId,
                                snapshotUrl = "https://picsum.photos/id/1/400/300",
                                position = DevicePosition(0.1f, 0.9f)
                            )
                        )
                        
                        bedroomDevices.forEach { device ->
                            val deviceId = device.name.lowercase().replace(" ", "_")
                            firestore.collection("devices").document(deviceId).set(device)
                        }
                    }

                // Kitchen on Ground Floor
                val kitchenId = "kitchen"
                val kitchen = Room(name = "Kitchen", floorId = groundFloorId)

                firestore.collection("rooms").document(kitchenId).set(kitchen)
                    .addOnSuccessListener {
                        println("SEEDER: Seeded Room: $kitchenId")

                        // Seed Devices for Kitchen
                        val kitchenDevices = listOf(
                            Device(
                                name = "kitchen_light_01",
                                type = "LIGHT",
                                status = "OFF",
                                roomId = kitchenId,
                                floorId = groundFloorId,
                                position = DevicePosition(0.4f, 0.4f)
                            ),
                            Device(
                                name = "kitchen_outlet_01",
                                type = "OUTLET",
                                status = "OFF",
                                roomId = kitchenId,
                                floorId = groundFloorId,
                                position = DevicePosition(0.6f, 0.6f)
                            )
                        )

                        kitchenDevices.forEach { device ->
                            firestore.collection("devices").document(device.name).set(device)
                        }
                    }
            }

        // Keep logic for Living Room / Study Room if they exist
        firestore.collection("rooms").get().addOnSuccessListener { roomsSnapshot ->
            val livingRoomId = roomsSnapshot.documents.find { it.getString("name")?.lowercase() == "living room" }?.id
            val studyRoomId = roomsSnapshot.documents.find { it.getString("name")?.lowercase() == "study room" }?.id

            if (livingRoomId != null && studyRoomId != null) {
                val otherDevices = listOf(
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
                    )
                )

                otherDevices.forEach { device ->
                    val documentId = device.name.lowercase().replace(" ", "_")
                    firestore.collection("devices").document(documentId).set(device)
                }
            }
        }
    }
}
