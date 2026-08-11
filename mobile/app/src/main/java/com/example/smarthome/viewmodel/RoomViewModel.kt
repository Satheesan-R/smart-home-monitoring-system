package com.example.smarthome.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smarthome.data.model.Room
import com.example.smarthome.data.repository.RoomRepository

class RoomViewModel : ViewModel() {

    private val repository = RoomRepository()

    var rooms by mutableStateOf<List<Room>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadRooms(floorId: String) {
        isLoading = true

        repository.getRoomsByFloor(
            floorId = floorId,
            onSuccess = { result ->
                rooms = result
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
    }
}
