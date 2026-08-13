package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.UsageRecord
import com.example.smarthome.data.repository.UsageRepository
import java.text.SimpleDateFormat
import java.util.*

class UsageViewModel : ViewModel() {
    private val repository = UsageRepository()

    var usageRecords by mutableStateOf<List<UsageRecord>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadTodayUsage()
    }

    fun loadTodayUsage() {
        isLoading = true
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        repository.getUsageByDate(
            date = today,
            onUpdate = { records ->
                usageRecords = records
                isLoading = false
            },
            onError = { e ->
                errorMessage = e.message
                isLoading = false
            }
        )
    }
}
