package com.codealyst.omanprayertimes.ui.screens.iqamah_times

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.ui.components.ScreenHeader

@Composable
fun IqamahTimesScreen(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    var iqamahTimesEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(title = "Iqamah Times")

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IqamahEnabledToggle(
                enabled = iqamahTimesEnabled,
                onEnabledChange = { iqamahTimesEnabled = it }
            )

            IqamahPrayerCard(
                prayerName = "Fajr",
                initialMinutesAfterAdhan = "20",
                enabled = iqamahTimesEnabled
            )
            IqamahPrayerCard(
                prayerName = "Dhuhr",
                initialMinutesAfterAdhan = "15",
                enabled = iqamahTimesEnabled
            )
            IqamahPrayerCard(
                prayerName = "Asr",
                initialMinutesAfterAdhan = "15",
                enabled = iqamahTimesEnabled
            )
            IqamahPrayerCard(
                prayerName = "Maghrib",
                initialMinutesAfterAdhan = "10",
                enabled = iqamahTimesEnabled
            )
            IqamahPrayerCard(
                prayerName = "Isha'a",
                initialMinutesAfterAdhan = "20",
                enabled = iqamahTimesEnabled
            )
        }
    }
}
