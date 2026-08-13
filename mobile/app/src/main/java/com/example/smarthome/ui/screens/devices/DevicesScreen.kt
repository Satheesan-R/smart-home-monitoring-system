package com.example.smarthome.ui.screens.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.ui.components.DeviceCard
import com.example.smarthome.ui.components.MultiSwitchCard
import com.example.smarthome.viewmodel.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    roomId: String,
    roomName: String,
    onBackClick: () -> Unit,
    onDeviceClick: (String) -> Unit,
    viewModel: RoomViewModel = viewModel()
) {
    println("DEVICES_SCREEN: roomId=$roomId, roomName=$roomName")
    LaunchedEffect(roomId) {
        viewModel.observeDevices(roomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(roomName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${viewModel.errorMessage}", color = MaterialTheme.colorScheme.error)
                }
            } else if (viewModel.devices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No devices found in this room.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.devices) { device ->
                        val cardModifier = Modifier.clickable { onDeviceClick(device.id) }
                        if (device.type.uppercase() == "MULTI_SWITCH") {
                            MultiSwitchCard(
                                device = device,
                                onSwitchToggle = { switchItem ->
                                    viewModel.toggleSwitch(device, switchItem)
                                },
                                modifier = cardModifier
                            )
                        } else {
                            DeviceCard(
                                device = device,
                                onToggle = { 
                                    viewModel.toggleDevice(device)
                                },
                                modifier = cardModifier
                            )
                        }
                    }
                }
            }
        }
    }
}
