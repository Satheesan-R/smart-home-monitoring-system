package com.example.smarthome.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadDevices()
    }

    Column(
        modifier = Modifier.padding(16.dp)
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

            viewModel.devices.forEach { device ->

                Text(
                    text = "${device.name} - ${device.status}"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
        }
    }
}
