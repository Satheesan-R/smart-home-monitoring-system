package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Floor
import com.example.smarthome.data.repository.FloorRepository

class FloorViewModel : ViewModel() {

    private val repository = FloorRepository()

    var floors by mutableStateOf<List<Floor>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadFloors()
    }

    fun loadFloors() {
        isLoading = true

        repository.getFloors(
            onSuccess = { result ->
                floors = result
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
    }
}
