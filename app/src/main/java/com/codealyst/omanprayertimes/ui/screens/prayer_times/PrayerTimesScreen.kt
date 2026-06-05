package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.settings.SettingsViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun PrayerTimesScreen(modifier: Modifier = Modifier) {
    // Oman's timezone
    val omanZone = ZoneId.of("Asia/Muscat")

    // Prayer times displayed for:
    var tableDate by rememberSaveable {
        mutableStateOf(LocalDate.now(omanZone).toString())
    }

    // Get prayer times view model
    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>();

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    // Every time a date is selected, fetch prayer times.
    LaunchedEffect(tableDate, settings.cityId) {
        prayerTimesViewModel.fetchPrayerTimesForDate(
            LocalDate.parse(tableDate),
            cityId = settings.cityId
        );
    }

    // Get the current date and time in Oman.
    var now by remember { mutableStateOf(ZonedDateTime.now(omanZone)) }

    LaunchedEffect(Unit) {
        while (true) {
            now = ZonedDateTime.now(omanZone)
            delay(1000)
        }
    }

    // Prepare the timer metadata based on current values of the prayer times and the time.
    val timerMetadata = getTimerMetadata(now, prayerTimesViewModel.state.value)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DateTimeSection(now = now)

        TimerSection(
            timerMetadata = timerMetadata
        )

        PrayerTimesTable(
            prayerTimesViewModel,
            timerMetadata ?: TimerMetadata("", false, 0),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        DateSelector(onDateSelected = { date -> tableDate = date.toString() })
    }
}
