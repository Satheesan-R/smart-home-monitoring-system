package com.example.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smarthome.ui.screens.dashboard.DashboardScreen
import com.example.smarthome.ui.screens.floors.FloorScreen
import com.example.smarthome.ui.screens.rooms.RoomScreen
import com.example.smarthome.ui.theme.SmartHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = currentRoute == "dashboard",
                    onClick = {
                        if (currentRoute != "dashboard") {
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Layers, contentDescription = "Floors") },
                    label = { Text("Floors") },
                    selected = currentRoute == "floors",
                    onClick = {
                        if (currentRoute != "floors") {
                            navController.navigate("floors") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
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
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
