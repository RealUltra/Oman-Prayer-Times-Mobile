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
import com.codealyst.omanprayertimes.features.settings.dtos.getIqamahTimes
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun PrayerTimesScreen(modifier: Modifier = Modifier) {
    // Prepare the current date and time in Oman.
    var now by remember { mutableStateOf(getOmanDateTime()) }

    // Prayer times displayed for:
    var tableDate by rememberSaveable { mutableStateOf(getOmanDate().toString()) }

    // Get the prayer times for the selected date.
    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>();
    val prayerTimesState = prayerTimesViewModel.state.value;
    val tablePrayerTimes = prayerTimesViewModel.getPrayerTimesForDate(LocalDate.parse(tableDate))

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    // Get the iqamah times for the selected date.
    val iqamahConfigs by settingsViewModel.iqamahConfigs.collectAsStateWithLifecycle()

    val tableIqamahTimes = if (settings.iqamahTimesEnabled && tablePrayerTimes != null) {
        iqamahConfigs.getIqamahTimes(tablePrayerTimes)
    } else {
        null
    }

    // Whenever the city is changed and at every second, update the next event info.
    val nextEvent: EventInfo? = run {
        val today = getOmanDate()
        val tomorrow = today.plusDays(1)

        val todayPrayerTimes = prayerTimesViewModel.getPrayerTimesForDate(today)
        val tomorrowPrayerTimes = prayerTimesViewModel.getPrayerTimesForDate(tomorrow)

        todayPrayerTimes?.let {
            val iqamahTimes =
                if (settings.iqamahTimesEnabled) {
                    iqamahConfigs.getIqamahTimes(it)
                } else {
                    null
                }

            getNextEvent(
                now,
                it,
                tomorrowPrayerTimes ?: it,
                iqamahTimes
            )
        }
    }

    // Get the current date and time in Oman
    LaunchedEffect(Unit) {
        while (true) {
            now = getOmanDateTime()
            delay(1000)
        }
    }

    // Every time the city is changed, fetch yearly prayer times.
    LaunchedEffect(settings.cityId) {
        prayerTimesViewModel.fetchYearlyPrayerTimes(cityId = settings.cityId);
    }




    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        DateTimeSection(now = now)

        NextEventTimer(nextEvent = nextEvent)

        PrayerTimesTable(
            tablePrayerTimes,
            tableIqamahTimes,
            nextEvent ?: EventInfo(),
            tableDate,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        DateSelector(onDateSelected = { date -> tableDate = date.toString() })
    }
}
