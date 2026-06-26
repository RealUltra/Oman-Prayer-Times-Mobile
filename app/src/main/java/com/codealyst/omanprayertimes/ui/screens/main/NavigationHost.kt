package com.codealyst.omanprayertimes.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codealyst.omanprayertimes.ui.screens.iqamah_config.IqamahConfigScreen
import com.codealyst.omanprayertimes.ui.screens.prayer_times.PrayerTimesScreen
import com.codealyst.omanprayertimes.ui.screens.settings.SettingsScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destination.PRAYER_TIMES.route,
        modifier = modifier
    ) {
        composable(Destination.PRAYER_TIMES.route) {
            PrayerTimesScreen(onConfigureIqamahTimes = { navController.navigate(SettingsRoute.IQAMAH_CONFIGS) })
        }

        composable(Destination.SETTINGS.route) {
            SettingsScreen(onConfigureIqamahTimes = { navController.navigate(SettingsRoute.IQAMAH_CONFIGS) })
        }

        composable(SettingsRoute.IQAMAH_CONFIGS) {
            IqamahConfigScreen()
        }
    }
}

private object SettingsRoute {
    const val IQAMAH_CONFIGS = "settings/iqamah_configs"
    const val REMINDERS = "settings/reminders"
}
