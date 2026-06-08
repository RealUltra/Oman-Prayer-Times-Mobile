package com.codealyst.omanprayertimes.ui.screens.main

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                        contentDescription = stringResource(destination.contentDescriptionRes)
                    )
                },
                label = { Text(stringResource(destination.labelRes)) }
            )
        }
    }
}

enum class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: Int,
    @StringRes val contentDescriptionRes: Int
) {
    PRAYER_TIMES(
        "prayer_times",
        R.string.prayer_times,
        R.drawable.ic_mosque,
        R.string.prayer_times
    ),
    SETTINGS(
        "settings",
        R.string.settings,
        R.drawable.ic_settings,
        R.string.settings
    )
}

private fun String?.isInDestination(destination: Destination): Boolean {
    return this == destination.route || this?.startsWith("${destination.route}/") == true
}
