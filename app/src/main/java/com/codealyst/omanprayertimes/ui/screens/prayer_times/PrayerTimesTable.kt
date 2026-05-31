package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrayerTimesTable(modifier: Modifier = Modifier) {
    Column(modifier) {
        PrayerTimeRow("Salah", "Adhan", "Iqamah", isHeader = true)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 2.dp)
        Spacer(Modifier.height(8.dp))

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            PrayerTimeRow("Fajr", "03:54 AM", "04:19 AM")
            PrayerTimeRow("Shurooq", "05:20 AM", "-")
            PrayerTimeRow("Dhuhr", "12:10 PM", "12:30 PM", highlighted = true)
            PrayerTimeRow("Asr", "03:29 PM", "03:49 PM")
            PrayerTimeRow("Maghrib", "06:54 PM", "06:59 PM")
            PrayerTimeRow("Isha'a", "08:15 PM", "08:35 PM")
        }
    }
}
