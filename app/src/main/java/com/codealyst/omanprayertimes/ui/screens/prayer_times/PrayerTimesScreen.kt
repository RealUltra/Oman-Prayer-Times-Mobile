package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrayerTimesScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DateTimeSection()
            DateSelector()
            PrayerTimesTable(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            TimerSection("Dhuhr", false, secondsLeft = 4500)
        }
    }
}

