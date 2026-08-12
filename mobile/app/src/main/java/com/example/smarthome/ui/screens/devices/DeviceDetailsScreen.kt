package com.example.smarthome.ui.screens.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.smarthome.data.model.Device
import com.example.smarthome.viewmodel.DeviceDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailsScreen(
    deviceId: String,
    onBackClick: () -> Unit,
    viewModel: DeviceDetailsViewModel = viewModel()
) {
    LaunchedEffect(deviceId) {
        viewModel.loadDevice(deviceId)
    }

    val device = viewModel.device

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(device?.name ?: "Device Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (device != null) {
                DeviceDetailsContent(
                    device = device,
                    onToggle = { viewModel.toggleDevice() },
                    onSwitchToggle = { viewModel.toggleSwitch(it) }
                )
            } else if (viewModel.errorMessage != null) {
                Text(
                    text = "Error: ${viewModel.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DeviceDetailsContent(
    device: Device,
    onToggle: () -> Unit,
    onSwitchToggle: (com.example.smarthome.data.model.SwitchItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val type = device.type.uppercase()
        val isOn = device.status.uppercase() == "ON"
        val statusColor = if (isOn) MaterialTheme.colorScheme.primary else Color.Gray

        // 1. Device Icon/Graphic
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(statusColor.copy(alpha = 0.1f), MaterialTheme.shapes.large),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (type) {
                    "LIGHT" -> Icons.Default.Lightbulb
                    "OUTLET" -> Icons.Default.Outlet
                    "IRON" -> Icons.Default.Fireplace
                    "CAMERA" -> Icons.Default.Videocam
                    "MULTI_SWITCH" -> Icons.Default.SettingsInputComponent
                    else -> Icons.Default.Devices
                },
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = statusColor
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Name and Status Text
        Text(
            text = device.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Status: ${device.status.uppercase()}",
            style = MaterialTheme.typography.titleMedium,
            color = statusColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Specialized Content
        when (type) {
            "MULTI_SWITCH" -> MultiSwitchUI(device, onSwitchToggle)
            "CAMERA" -> CameraUI(device)
            "IRON" -> IronUI(device, onToggle)
            "LIGHT", "OUTLET" -> NormalDeviceUI(device, onToggle)
            else -> NormalDeviceUI(device, onToggle)
        }
    }
}

@Composable
fun NormalDeviceUI(device: Device, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(if (device.status.uppercase() == "ON") "TURN OFF" else "TURN ON")
        }
    }
}

@Composable
fun IronUI(device: Device, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val maxDurationSeconds = device.maxOnDurationLong
        val turnedOnAt = device.turnedOnAtTimestamp
        
        // 1. Safety Information
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Safety Information", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                DetailRow("Maximum Duration", "${maxDurationSeconds / 60} minutes")
                
                if (device.status.uppercase() == "ON" && turnedOnAt != null) {
                    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
                    val startTime = timeFormatter.format(turnedOnAt.toDate())
                    
                    val elapsedSeconds = (System.currentTimeMillis() / 1000) - turnedOnAt.seconds
                    val elapsedMinutes = elapsedSeconds / 60
                    
                    DetailRow("Started At", startTime)
                    DetailRow("Running", "$elapsedMinutes minutes", isWarning = elapsedSeconds > maxDurationSeconds)
                }
            }
        }
        
        Button(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (device.status.uppercase() == "ON") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (device.status.uppercase() == "ON") "TURN OFF" else "TURN ON")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isWarning: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value, 
            style = MaterialTheme.typography.bodyLarge, 
            fontWeight = FontWeight.Bold,
            color = if (isWarning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MultiSwitchUI(
    device: Device,
    onSwitchToggle: (com.example.smarthome.data.model.SwitchItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            device.switches.forEach { switch ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = switch.name, style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = switch.status.uppercase() == "ON",
                        onCheckedChange = { onSwitchToggle(switch) }
                    )
                }
            }
        }
    }
}

@Composable
fun CameraUI(device: Device) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            if (device.status.uppercase() == "ON" && !device.snapshotUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = device.snapshotUrl,
                    contentDescription = "Camera Snapshot",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = "No Signal", color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Full screen */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = device.status.uppercase() == "ON"
        ) {
            Text("VIEW CAMERA")
        }
    }
}
