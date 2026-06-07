package com.codealyst.omanprayertimes.ui.screens.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.codealyst.omanprayertimes.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Destination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute.isInDestination(destination),
                onClick = {
                    navController.navigate(
                        route = destination.route,
                        navOptions = navOptions {
                            popUpTo(navController.graph.findStartDestination().id) {
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = destination.contentDescription
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String
) {
    PRAYER_TIMES("prayer_times", "Prayer Times", R.drawable.ic_mosque, "Prayer Times"),
    SETTINGS("settings", "Settings", R.drawable.ic_settings, "Settings")
}

private fun String?.isInDestination(destination: Destination): Boolean {
    return this == destination.route || this?.startsWith("${destination.route}/") == true
}
