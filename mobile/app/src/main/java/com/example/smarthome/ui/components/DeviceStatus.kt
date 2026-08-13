package com.example.smarthome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeviceStatus(status: String, modifier: Modifier = Modifier) {
    val upperStatus = status.uppercase()
    val color = getStatusColor(upperStatus)
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (upperStatus == "ERROR") {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
        }
        
        Text(
            text = upperStatus,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {
        "ON" -> MaterialTheme.colorScheme.primary
        "OFF" -> Color.Gray
        "ERROR" -> MaterialTheme.colorScheme.error
        "DISCONNECTED" -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
