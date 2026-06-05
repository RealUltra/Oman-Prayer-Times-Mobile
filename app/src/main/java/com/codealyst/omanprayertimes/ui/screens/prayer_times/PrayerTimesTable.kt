package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.UiState
import com.codealyst.omanprayertimes.features.settings.IqamahSetting
import com.codealyst.omanprayertimes.features.settings.PrayerKeys
import com.codealyst.omanprayertimes.features.settings.get
import com.codealyst.omanprayertimes.features.settings.getIqamahTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PrayerTimesTable(
    prayerTimesViewModel: PrayerTimesViewModel,
    iqamahSettings: List<IqamahSetting>,
    timerMetadata: TimerMetadata,
    modifier: Modifier = Modifier,
) {
    // Get prayer times
    val state = prayerTimesViewModel.state.value

    val fajrTime = if (state is UiState.Success) convertTo12Hour(state.data.fajrTime) else "-"
    val shurooqTime = if (state is UiState.Success) convertTo12Hour(state.data.shurooqTime) else "-"
    val dhuhrTime = if (state is UiState.Success) convertTo12Hour(state.data.dhuhrTime) else "-"
    val asrTime = if (state is UiState.Success) convertTo12Hour(state.data.asrTime) else "-"
    val maghribTime = if (state is UiState.Success) convertTo12Hour(state.data.maghribTime) else "-"
    val ishaTime = if (state is UiState.Success) convertTo12Hour(state.data.ishaaTime) else "-"

    val fajrIqamahTime = iqamahSettings.get(PrayerKeys.FAJR)?.getIqamahTime(fajrTime) ?: "-"
    val dhuhrIqamahTime = iqamahSettings.get(PrayerKeys.DHUHR)?.getIqamahTime(dhuhrTime) ?: "-"
    val asrIqamahTime = iqamahSettings.get(PrayerKeys.ASR)?.getIqamahTime(asrTime) ?: "-"
    val maghribIqamahTime =
        iqamahSettings.get(PrayerKeys.MAGHRIB)?.getIqamahTime(maghribTime) ?: "-"
    val ishaIqamahTime = iqamahSettings.get(PrayerKeys.ISHA)?.getIqamahTime(ishaTime) ?: "-"

    val colorScheme = MaterialTheme.colorScheme;

    Column(
        modifier

    ) {
        PrayerTimeRow("Salah", "Adhan", "Iqamah", isHeader = true)
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 2.dp)
        Spacer(Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PrayerTimeRow(
                "Fajr",
                fajrTime,
                fajrIqamahTime,
                highlighted = (timerMetadata.salahName == "Fajr"),
                isAdhanNext = timerMetadata.isAdhan ?: false
            )
            PrayerTimeRow(
                "Shurooq",
                shurooqTime,
                "-",
                highlighted = (timerMetadata.salahName == "Shurooq"),
                isAdhanNext = true
            )
            PrayerTimeRow(
                "Dhuhr",
                dhuhrTime,
                dhuhrIqamahTime,
                highlighted = (timerMetadata.salahName == "Dhuhr"),
                isAdhanNext = timerMetadata.isAdhan ?: false
            )
            PrayerTimeRow(
                "Asr",
                asrTime,
                asrIqamahTime,
                highlighted = (timerMetadata.salahName == "Asr"),
                isAdhanNext = timerMetadata.isAdhan ?: false
            )
            PrayerTimeRow(
                "Maghrib",
                maghribTime,
                maghribIqamahTime,
                highlighted = (timerMetadata.salahName == "Maghrib"),
                isAdhanNext = timerMetadata.isAdhan ?: false
            )
            PrayerTimeRow(
                "Isha'a",
                ishaTime,
                ishaIqamahTime,
                highlighted = (timerMetadata.salahName == "Isha'a"),
                isAdhanNext = timerMetadata.isAdhan ?: false
            )
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
