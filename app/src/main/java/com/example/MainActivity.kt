package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.screens.AddItemScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ItemDetailsScreen
import com.example.ui.screens.BookingDialog
import com.example.ui.screens.DiscoveryScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.theme.RentAnythingTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.SmartToy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentAnythingTheme {
                val appContainer = (application as RybApplication).container
                val factory = MainViewModelFactory(
                    appContainer.database.itemDao(), 
                    appContainer.database.bookingDao(),
                    appContainer.database.userDao(),
                    appContainer.database.reviewDao(),
                    appContainer.repository
                )
                val viewModel: MainViewModel = viewModel(factory = factory)
                RentAnythingApp(viewModel)
            }
        }
    }
}

@Composable
fun RentAnythingApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingItem by remember { mutableStateOf<com.example.model.Item?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentDestination?.route in listOf("discovery", "my_bookings", "profile", "ai_assistant")) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentDestination?.hierarchy?.any { it.route == "discovery" } == true,
                        onClick = {
                            navController.navigate("discovery") {
                                popUpTo("discovery") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "My Rentals") },
                        label = { Text("My Rentals") },
                        selected = currentDestination?.hierarchy?.any { it.route == "my_bookings" } == true,
                        onClick = {
                            navController.navigate("my_bookings") {
                                popUpTo("discovery") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI Assistant") },
                        label = { Text("AI Assistant") },
                        selected = currentDestination?.hierarchy?.any { it.route == "ai_assistant" } == true,
                        onClick = {
                            navController.navigate("ai_assistant") {
                                popUpTo("discovery") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                        onClick = {
                            navController.navigate("profile") {
                                popUpTo("discovery") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(viewModel = viewModel, onLoginSuccess = {
                    navController.navigate("discovery") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("discovery") {
                DiscoveryScreen(
                    viewModel = viewModel,
                    onItemClick = { itemId ->
                        navController.navigate("details/$itemId")
                    }
                )
            }
            composable("my_bookings") {
                MyBookingsScreen(
                    viewModel = viewModel,
                    onTrackRentalClick = { bookingId ->
                        navController.navigate("tracking/$bookingId")
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel,
                    onAdminClick = {
                        navController.navigate("admin")
                    },
                    onAddItemClick = {
                        navController.navigate("add_item")
                    }
                )
            }
            composable("ai_assistant") {
                AiAssistantScreen(
                    viewModel = viewModel
                )
            }
            composable("add_item") {
                AddItemScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("admin") {
                AdminScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "tracking/{bookingId}",
                arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: return@composable
                LiveTrackingScreen(
                    bookingId = bookingId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "details/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
                ItemDetailsScreen(
                    itemId = itemId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookNow = {
                        bookingItem = viewModel.selectedItem.value
                        showBookingDialog = true
                    }
                )
            }
        }

        if (showBookingDialog && bookingItem != null) {
            BookingDialog(
                item = bookingItem!!,
                viewModel = viewModel,
                onDismiss = {
                    showBookingDialog = false
                    bookingItem = null
                },
                onSuccess = {
                    showBookingDialog = false
                    bookingItem = null
                    navController.navigate("my_bookings") {
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
            )
        }
    }
}
