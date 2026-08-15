package com.example.smarthome.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.ui.components.DeviceCard
import com.example.smarthome.ui.components.MultiSwitchCard
import com.example.smarthome.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onDeviceClick: (String) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.observeDevices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Smart Home",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else if (viewModel.errorMessage != null) {
            Text(
                text = "Error: ${viewModel.errorMessage}"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
