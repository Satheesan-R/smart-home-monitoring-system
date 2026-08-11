package com.example.smarthome.data.repository

import com.example.smarthome.data.model.Floor
import com.google.firebase.firestore.FirebaseFirestore

class FloorRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getFloors(
        onSuccess: (List<Floor>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("floors")
            .get()
            .addOnSuccessListener { result ->
                val floors = result.documents.mapNotNull { document ->
                    document.toObject(Floor::class.java)
                        ?.copy(id = document.id)
                }
                onSuccess(floors)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
