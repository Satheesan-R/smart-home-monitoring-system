package com.example.smarthome

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.*
import com.example.smarthome.worker.SafetyWorker
import java.util.concurrent.TimeUnit

class SmartHomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setupSafetyWorker()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Safety Alerts"
            val descriptionText = "Notifications for automatic safety cutoffs"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("SAFETY_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupSafetyWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val safetyRequest = PeriodicWorkRequestBuilder<SafetyWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SafetyCutoffWork",
            ExistingPeriodicWorkPolicy.KEEP,
            safetyRequest
        )
    }
}
