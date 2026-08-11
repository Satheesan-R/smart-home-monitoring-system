package com.example.smarthome.data.repository

import com.example.smarthome.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore

class RoomRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getRoomsByFloor(
        floorId: String,
        onSuccess: (List<Room>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("rooms")
            .whereEqualTo("floorId", floorId)
            .get()
            .addOnSuccessListener { result ->
                val rooms = result.documents.mapNotNull { document ->
                    document.toObject(Room::class.java)
                        ?.copy(id = document.id)
                }
                onSuccess(rooms)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
