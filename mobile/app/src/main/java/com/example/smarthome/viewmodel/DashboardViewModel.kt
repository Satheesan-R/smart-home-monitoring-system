package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Device
import com.example.smarthome.data.repository.DeviceRepository

class DashboardViewModel : ViewModel() {

    private val repository = DeviceRepository()

    var devices by mutableStateOf<List<Device>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadDevices()
    }

    fun loadDevices() {
        isLoading = true
        errorMessage = null

        repository.getDevices(
            onSuccess = { result ->
                devices = result
                isLoading = false
            },
            onError = { exception ->
                errorMessage = exception.message
                isLoading = false
            }
        )
    }
}
