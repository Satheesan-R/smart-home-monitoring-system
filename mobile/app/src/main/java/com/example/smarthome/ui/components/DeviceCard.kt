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
import com.example.smarthome.data.model.Device

@Composable
fun DeviceCard(
    device: Device,
    onToggle: () -> Unit,
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
                    MultiSwitchContent(device)
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
fun MultiSwitchContent(device: Device) {
    device.switches?.forEach { (key, value) ->
        val switchName = value["name"] ?: key
        val switchStatus = value["status"] ?: "OFF"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = switchName, style = MaterialTheme.typography.bodyMedium)
            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (switchStatus == "ON") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = switchStatus,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun CameraContent(device: Device) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.DarkGray, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "[ Mock Snapshot ]", color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* TODO: Implement view camera */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Camera")
        }
    }
}

@Composable
fun IronContent(device: Device, onToggle: () -> Unit) {
    Column {
        device.maxOnDuration?.let {
            Text(text = "Maximum: $it minutes", style = MaterialTheme.typography.bodySmall)
        }
        if (device.status.uppercase() == "ON") {
            // Logic to calculate running time would go here
            Text(text = "Running: -- minutes", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Switch(
            checked = device.status.uppercase() == "ON",
            onCheckedChange = { onToggle() },
            modifier = Modifier.align(Alignment.End)
        )
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
