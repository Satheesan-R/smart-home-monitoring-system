package com.example.smarthome.data.firebase

import com.example.smarthome.data.model.*
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSeeder {
    fun seedData() {
        println("SEEDER: Starting seeding...")
        val firestore = FirebaseFirestore.getInstance()

        // 1. Seed Floors
        val floors = mapOf(
            "ground_floor" to Floor(name = "Ground Floor", floorNumber = 0, floorPlanUrl = ""),
            "first_floor" to Floor(name = "First Floor", floorNumber = 1, floorPlanUrl = "")
        )

        floors.forEach { (id, floor) ->
            firestore.collection("floors").document(id).set(floor)
        }

        // 2. Seed Rooms
        val rooms = mapOf(
            "living_room" to Room(name = "Living Room", floorId = "ground_floor"),
            "kitchen" to Room(name = "Kitchen", floorId = "ground_floor"),
            "bedroom" to Room(name = "Bedroom", floorId = "first_floor"),
            "study_room" to Room(name = "Study Room", floorId = "first_floor")
        )

        rooms.forEach { (id, room) ->
            firestore.collection("rooms").document(id).set(room)
        }

        // 3. Seed Devices
        val devices = mapOf(
            "living_light_01" to Device(
                name = "Living Room Light",
                type = "LIGHT",
                status = "OFF",
                floorId = "ground_floor",
                roomId = "living_room",
                position = DevicePosition(60f, 30f)
            ),
            "living_switch_01" to Device(
                name = "Living Room Switch",
                type = "MULTI_SWITCH",
                status = "OFF",
                floorId = "ground_floor",
                roomId = "living_room",
                switchCount = 3,
                switches = listOf(
                    SwitchItem(id = "switch_1", name = "Main Light", status = "OFF"),
                    SwitchItem(id = "switch_2", name = "Fan", status = "OFF"),
                    SwitchItem(id = "switch_3", name = "Lamp", status = "OFF")
                )
            ),
            "living_camera_01" to Device(
                name = "Living Room Camera",
                type = "CAMERA",
                status = "ON",
                floorId = "ground_floor",
                roomId = "living_room",
                snapshotUrl = ""
            ),
            "kitchen_light_01" to Device(
                name = "Kitchen Light",
                type = "LIGHT",
                status = "OFF",
                floorId = "ground_floor",
                roomId = "kitchen"
            ),
            "kitchen_outlet_01" to Device(
                name = "Kitchen Outlet",
                type = "OUTLET",
                status = "OFF",
                floorId = "ground_floor",
                roomId = "kitchen"
            ),
            "bedroom_light_01" to Device(
                name = "Bedroom Light",
                type = "LIGHT",
                status = "OFF",
                floorId = "first_floor",
                roomId = "bedroom"
            ),
            "bedroom_iron_01" to Device(
                name = "Bedroom Iron",
                type = "IRON",
                status = "OFF",
                floorId = "first_floor",
                roomId = "bedroom",
                maxOnDuration = 1800,
                turnedOnAt = null
            ),
            "bedroom_outlet_01" to Device(
                name = "Bedroom Outlet",
                type = "OUTLET",
                status = "OFF",
                floorId = "first_floor",
                roomId = "bedroom"
            ),
            "bedroom_camera_01" to Device(
                name = "Bedroom Camera",
                type = "CAMERA",
                status = "ON",
                floorId = "first_floor",
                roomId = "bedroom",
                snapshotUrl = ""
            ),
            "study_light_01" to Device(
                name = "Study Room Light",
                type = "LIGHT",
                status = "OFF",
                floorId = "first_floor",
                roomId = "study_room"
            ),
            "study_switch_01" to Device(
                name = "Study Room Switch",
                type = "MULTI_SWITCH",
                status = "OFF",
                floorId = "first_floor",
                roomId = "study_room",
                switchCount = 2,
                switches = listOf(
                    SwitchItem(id = "switch_1", name = "Light", status = "OFF"),
                    SwitchItem(id = "switch_2", name = "Fan", status = "OFF")
                )
            )
        )

        devices.forEach { (id, device) ->
            firestore.collection("devices").document(id).set(device)
        }

        // 4. Seed Schedules
        val schedules = mapOf(
            "bedroom_light_schedule_01" to Schedule(
                deviceId = "bedroom_light_01",
                enabled = true,
                onTime = "18:00",
                offTime = "23:00"
            )
        )

        schedules.forEach { (id, schedule) ->
            firestore.collection("schedules").document(id).set(schedule)
        }
        
        println("SEEDER: Seeding complete.")
    }
}
