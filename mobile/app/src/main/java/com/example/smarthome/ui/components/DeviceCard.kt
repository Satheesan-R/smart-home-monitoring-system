package com.example.smarthome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.smarthome.data.model.Device

@Composable
fun DeviceCard(
    device: Device,
    onToggle: () -> Unit,
    onSwitchToggle: (com.example.smarthome.data.model.SwitchItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val icon = when (device.type.uppercase()) {
        "LIGHT" -> Icons.Default.Lightbulb
        "OUTLET" -> Icons.Default.Outlet
        "IRON" -> Icons.Default.Fireplace
        "CAMERA" -> Icons.Default.Videocam
        "MULTI_SWITCH" -> Icons.Default.SettingsInputComponent
        else -> Icons.Default.Devices
    }

    val statusColor = when (device.status.uppercase()) {
        "ON" -> MaterialTheme.colorScheme.primary
        "OFF" -> MaterialTheme.colorScheme.secondary
        "ERROR" -> MaterialTheme.colorScheme.error
        "DISCONNECTED" -> Color.Gray
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Icon, Name, and general Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = device.status.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body: Specialized content based on device type
            when (device.type.uppercase()) {
                "MULTI_SWITCH" -> {
                    MultiSwitchContent(device, onSwitchToggle)
                }
                "CAMERA" -> {
                    CameraContent(device)
                }
                "IRON" -> {
                    IronContent(device, onToggle)
                }
                else -> {
                    DefaultDeviceContent(device, onToggle)
                }
            }
        }
    }
}

@Composable
fun MultiSwitchContent(
    device: Device,
    onSwitchToggle: (com.example.smarthome.data.model.SwitchItem) -> Unit
) {
    device.switches.forEach { switch ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = switch.name, style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = switch.status.uppercase() == "ON",
                onCheckedChange = { onSwitchToggle(switch) }
            )
        }
    }
}

@Composable
fun CameraContent(device: Device) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
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
                // Overlay "LIVE" indicator
                Surface(
                    color = Color.Red,
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "LIVE",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideocamOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = if (device.status.uppercase() == "OFF") "Camera Offline" else "No Feed Available",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* TODO: Open full screen view */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = device.status.uppercase() == "ON"
        ) {
            Text("View Camera")
        }
    }
}

@Composable
fun IronContent(device: Device, onToggle: () -> Unit) {
    Column {
        val maxDuration = device.maxOnDurationLong
        if (maxDuration > 0) {
            Text(text = "Safety Cutoff: $maxDuration mins", style = MaterialTheme.typography.bodySmall)
        }
        
        if (device.status.uppercase() == "ON") {
            val turnedOnAt = device.turnedOnAtTimestamp
            if (turnedOnAt != null) {
                val durationMillis = System.currentTimeMillis() - turnedOnAt.toDate().time
                val durationMinutes = durationMillis / (1000 * 60)
                Text(
                    text = "Running for: $durationMinutes mins", 
                    style = MaterialTheme.typography.bodySmall,
                    color = if (durationMinutes > maxDuration && maxDuration > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Switch(
                checked = device.status.uppercase() == "ON",
                onCheckedChange = { onToggle() },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun DefaultDeviceContent(device: Device, onToggle: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Switch(
            checked = device.status.uppercase() == "ON",
            onCheckedChange = { onToggle() },
            enabled = device.status.uppercase() != "DISCONNECTED",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
