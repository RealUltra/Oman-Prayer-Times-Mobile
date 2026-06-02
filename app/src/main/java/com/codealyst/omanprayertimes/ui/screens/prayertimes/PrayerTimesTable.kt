package com.codealyst.omanprayertimes.ui.screens.prayertimes

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.codealyst.omanprayertimes.features.prayertimes.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.prayertimes.viewmodels.UiState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PrayerTimesTable(modifier: Modifier = Modifier) {
    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>();
    val state = prayerTimesViewModel.state.value

    prayerTimesViewModel.fetchPrayerTimesForDate(LocalDate.now(ZoneId.of("Asia/Muscat")))

    val fajrTime = if (state is UiState.Success) convertTo12Hour(state.data.fajrTime) else "-"
    val shurooqTime = if (state is UiState.Success) convertTo12Hour(state.data.shurooqTime) else "-"
    val dhuhrTime = if (state is UiState.Success) convertTo12Hour(state.data.dhuhrTime) else "-"
    val asrTime = if (state is UiState.Success) convertTo12Hour(state.data.asrTime) else "-"
    val maghribTime = if (state is UiState.Success) convertTo12Hour(state.data.maghribTime) else "-"
    val ishaaTime = if (state is UiState.Success) convertTo12Hour(state.data.ishaaTime) else "-"

    Column(modifier) {
        PrayerTimeRow("Salah", "Adhan", "Iqamah", isHeader = true)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 2.dp)
        Spacer(Modifier.height(8.dp))

        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            PrayerTimeRow("Fajr", fajrTime, "04:19 AM")
            PrayerTimeRow("Shurooq", shurooqTime, "-")
            PrayerTimeRow("Dhuhr", dhuhrTime, "12:30 PM", highlighted = true, isAdhanNext = true)
            PrayerTimeRow("Asr", asrTime, "03:49 PM")
            PrayerTimeRow("Maghrib", maghribTime, "06:59 PM")
            PrayerTimeRow("Isha'a", ishaaTime, "08:35 PM")
        }
    }
}

fun convertTo12Hour(time: String): String {
    return try {
        LocalTime
            .parse(time, DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
            .format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    } catch (_: Exception) {
        time
    }
}