package com.example.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smarthome.data.firebase.FirebaseSeeder
import com.example.smarthome.ui.screens.alerts.AlertsScreen
import com.example.smarthome.ui.screens.dashboard.DashboardScreen
import com.example.smarthome.ui.screens.devices.DevicesScreen
import com.example.smarthome.ui.screens.floors.FloorScreen
import com.example.smarthome.ui.screens.reports.ReportsScreen
import com.example.smarthome.ui.screens.rooms.RoomScreen
import com.example.smarthome.ui.screens.schedules.SchedulesScreen
import com.example.smarthome.ui.theme.SmartHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Seed data once to ensure required devices exist
        FirebaseSeeder.seedData()

        setContent {
            SmartHomeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    Triple("dashboard", "Dashboard", Icons.Default.Home),
                    Triple("floors", "Floors", Icons.Default.Layers),
                    Triple("schedules", "Schedules", Icons.Default.Schedule),
                    Triple("alerts", "Alerts", Icons.Default.Notifications),
                    Triple("reports", "Reports", Icons.Default.Assessment)
                )

                items.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentRoute == route,
                        onClick = {
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen()
            }
            composable("floors") {
                FloorScreen(
                    onFloorClick = { floorId, floorName ->
                        navController.navigate("rooms/$floorId/$floorName")
                    }
                )
            }
            composable("schedules") {
                SchedulesScreen()
            }
            composable("alerts") {
                AlertsScreen()
            }
            composable("reports") {
                ReportsScreen()
            }
            composable(
                route = "rooms/{floorId}/{floorName}",
                arguments = listOf(
                    navArgument("floorId") { type = NavType.StringType },
                    navArgument("floorName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
                val floorName = backStackEntry.arguments?.getString("floorName") ?: ""
                RoomScreen(
                    floorId = floorId,
                    floorName = floorName,
                    onBackClick = { navController.popBackStack() },
                    onRoomClick = { roomId, roomName ->
                        navController.navigate("devices/$roomId/$roomName")
                    }
                )
            }
            composable(
                route = "devices/{roomId}/{roomName}",
                arguments = listOf(
                    navArgument("roomId") { type = NavType.StringType },
                    navArgument("roomName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
                DevicesScreen(
                    roomId = roomId,
                    roomName = roomName,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
