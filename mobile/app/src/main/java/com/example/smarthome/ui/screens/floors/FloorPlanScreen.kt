package com.example.smarthome.ui.screens.floors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.data.model.Device
import com.example.smarthome.viewmodel.FloorPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorPlanScreen(
    floorId: String,
    onBackClick: () -> Unit,
    viewModel: FloorPlanViewModel = viewModel()
) {
    LaunchedEffect(floorId) {
        viewModel.loadFloorData(floorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.floorName) },
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
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                FloorPlanContent(
                    devices = viewModel.devices,
                    onDeviceClick = { viewModel.toggleDevice(it) }
                )
            }
        }
    }
}

@Composable
fun FloorPlanContent(
    devices: List<Device>,
    onDeviceClick: (Device) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val fullWidth = maxWidth
        val fullHeight = maxHeight

        // 1. Draw Grid Background
        GridBackground()

        // 2. Draw Rooms (Placeholder for now)

        // 3. Draw Device Icons
        devices.forEach { device ->
            DeviceMarker(
                device = device,
                modifier = Modifier.offset(
                    x = fullWidth * device.position.x - 24.dp,
                    y = fullHeight * device.position.y - 24.dp
                ),
                onClick = { onDeviceClick(device) }
            )
        }
    }
}

@Composable
fun GridBackground() {
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 40.dp.toPx()
        for (x in 0..size.width.toInt() step step.toInt()) {
            drawLine(
                color = gridColor,
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
        for (y in 0..size.height.toInt() step step.toInt()) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
fun DeviceMarker(
    device: Device,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = getDeviceIcon(device.type)
    val isOn = device.status.uppercase() == "ON"
    val color = if (isOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Surface(
        modifier = modifier
            .size(48.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = if (isOn) color.copy(alpha = 0.2f) else Color.Transparent,
        border = if (isOn) null else androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = device.name,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun getDeviceIcon(type: String): ImageVector {
    return when (type.uppercase()) {
        "LIGHT" -> Icons.Default.Lightbulb
        "OUTLET" -> Icons.Default.Outlet
        "IRON" -> Icons.Default.Fireplace
        "CAMERA" -> Icons.Default.Videocam
        "MULTI_SWITCH" -> Icons.Default.SettingsInputComponent
        else -> Icons.Default.Devices
    }
}
