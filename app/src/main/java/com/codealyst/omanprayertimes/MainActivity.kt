package com.codealyst.omanprayertimes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codealyst.omanprayertimes.ui.screens.prayertimes.PrayerTimesScreen
import com.codealyst.omanprayertimes.ui.theme.OmanPrayerTimesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OmanPrayerTimesTheme {
                PrayerTimesScreen()
            }
        }
    }
}
