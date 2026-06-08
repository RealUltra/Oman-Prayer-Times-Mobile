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
import com.codealyst.omanprayertimes.features.oman_datetime.getOmanDate
import com.codealyst.omanprayertimes.features.oman_datetime.getOmanDateTime
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.UiState
import com.codealyst.omanprayertimes.features.settings.dtos.getIqamahTimes
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun PrayerTimesScreen(modifier: Modifier = Modifier) {
    // Prayer times displayed for:
    var tableDate by rememberSaveable {
        mutableStateOf(getOmanDate().toString())
    }

    // Get prayer times
    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>();
    val prayerTimesState = prayerTimesViewModel.state.value;
    val prayerTimes = if (prayerTimesState is UiState.Success) prayerTimesState.data else null;

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    // Get iqamah times
    val iqamahConfigs by settingsViewModel.iqamahConfigs.collectAsStateWithLifecycle()
    val iqamahTimes =
        if (settings.iqamahTimesEnabled && prayerTimes != null) {
            iqamahConfigs.getIqamahTimes(prayerTimes)
        } else {
            null
        }

    // Every time a date is selected, fetch prayer times.
    LaunchedEffect(tableDate, settings.cityId) {
        prayerTimesViewModel.fetchPrayerTimesForDate(
            LocalDate.parse(tableDate),
            cityId = settings.cityId
        );
    }

    // Get the current date and time in Oman.
    var now by remember { mutableStateOf(getOmanDateTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = getOmanDateTime()
            delay(1000)
        }
    }

    // Retrieve the next event based on prayer times and the current time.
    val nextEvent =
        if (prayerTimes != null) {
            getNextEvent(now, prayerTimes, prayerTimes, iqamahTimes)
        } else {
            null
        }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        DateTimeSection(now = now)

        NextEventTimer(nextEvent = nextEvent)

        PrayerTimesTable(
            prayerTimes,
            iqamahTimes,
            nextEvent ?: EventInfo(),
            tableDate,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        DateSelector(onDateSelected = { date -> tableDate = date.toString() })
    }
}
