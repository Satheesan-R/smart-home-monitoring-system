package com.example.smarthome.data.repository

import com.example.smarthome.data.model.UsageRecord
import com.google.firebase.firestore.FirebaseFirestore

class UsageRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getUsageByDate(
        date: String,
        onUpdate: (List<UsageRecord>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("usage")
            .whereEqualTo("date", date)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val records = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(UsageRecord::class.java)?.copy(id = doc.id)
                    }
                    onUpdate(records)
                }
            }
    }
}
