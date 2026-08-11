package com.example.smarthome.ui.screens.floors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.data.model.Floor
import com.example.smarthome.viewmodel.FloorViewModel

@Composable
fun FloorScreen(
    onFloorClick: (String, String) -> Unit,
    viewModel: FloorViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadFloors()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Floors",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (viewModel.floors.isEmpty()) {
            Text(text = "No floors found.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.floors) { floor ->
                    FloorItem(
                        floor = floor,
                        onClick = { onFloorClick(floor.id, floor.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun FloorItem(floor: Floor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = floor.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Floor Number: ${floor.floorNumber ?: ""}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
