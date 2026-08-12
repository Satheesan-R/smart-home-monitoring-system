package com.example.smarthome.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smarthome.R
import com.example.smarthome.data.model.Device
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

class SafetyWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val firestore = FirebaseFirestore.getInstance()
        
        try {
            val snapshot = firestore.collection("devices")
                .whereEqualTo("status", "ON")
                .get()
                .await()

            val now = Timestamp.now().seconds

            snapshot.documents.forEach { doc ->
                val deviceName = doc.getString("name") ?: "Device"
                val turnedOnAt = doc.get("turnedOnAt") as? Timestamp
                val maxOnDuration = (doc.get("maxOnDuration") as? Number)?.toLong() ?: 0L

                if (turnedOnAt != null && maxOnDuration > 0) {
                    val onDurationSeconds = now - turnedOnAt.seconds
                    val maxDurationSeconds = maxOnDuration * 60

                    if (onDurationSeconds > maxDurationSeconds) {
                        println("SAFETY: Device $deviceName exceeded max duration. Turning off.")
                        doc.reference.update(
                            "status", "OFF",
                            "turnedOnAt", null
                        ).await()
                        
                        sendNotification(deviceName)
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            println("SAFETY: Worker failed: ${e.message}")
            return Result.retry()
        }
    }

    private fun sendNotification(deviceName: String) {
        val builder = NotificationCompat.Builder(applicationContext, "SAFETY_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Safety Cutoff")
            .setContentText("$deviceName was automatically turned off after exceeding safe usage time.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            try {
                notify(System.currentTimeMillis().toInt(), builder.build())
            } catch (e: SecurityException) {
                // Handle missing permission on Android 13+
            }
        }
    }
}
