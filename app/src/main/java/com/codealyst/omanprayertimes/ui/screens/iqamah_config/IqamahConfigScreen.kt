package com.codealyst.omanprayertimes.ui.screens.iqamah_config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.UiState
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import com.codealyst.omanprayertimes.features.settings.dtos.PrayerKeys
import com.codealyst.omanprayertimes.features.settings.dtos.get
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.ScreenHeader
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun IqamahConfigScreen(modifier: Modifier = Modifier) {
    // Get prayer times
    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>()
    val prayerTimesState = prayerTimesViewModel.state.value
    val prayerTimes = if (prayerTimesState is UiState.Success) prayerTimesState.data else null

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
    val iqamahConfigs by settingsViewModel.iqamahConfigs.collectAsStateWithLifecycle()

    val omanZone = ZoneId.of("Asia/Muscat")

    LaunchedEffect(settings.cityId) {
        prayerTimesViewModel.fetchPrayerTimesForDate(
            LocalDate.now(omanZone),
            cityId = settings.cityId
        )
    }

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

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
                enabled = settings.iqamahTimesEnabled,
                onEnabledChange = { settingsViewModel.setIqamahTimesEnabled(it) }
            )

            if (settings.iqamahTimesEnabled) {
                IqamahConfigCard(
                    prayerKey = PrayerKeys.FAJR,
                    initialMode = iqamahConfigs.initialMode(PrayerKeys.FAJR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKeys.FAJR,
                        "25"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKeys.FAJR,
                        prayerTimes?.fajrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKeys.DHUHR,
                    initialMode = iqamahConfigs.initialMode(PrayerKeys.DHUHR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKeys.DHUHR,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKeys.DHUHR,
                        prayerTimes?.dhuhrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKeys.ASR,
                    initialMode = iqamahConfigs.initialMode(PrayerKeys.ASR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKeys.ASR,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKeys.ASR,
                        prayerTimes?.asrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKeys.MAGHRIB,
                    initialMode = iqamahConfigs.initialMode(PrayerKeys.MAGHRIB),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKeys.MAGHRIB,
                        "5"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKeys.MAGHRIB,
                        prayerTimes?.maghribTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKeys.ISHA,
                    initialMode = iqamahConfigs.initialMode(PrayerKeys.ISHA),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKeys.ISHA,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKeys.ISHA,
                        prayerTimes?.ishaTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
            }
        }
    }
}

private const val DEFAULT_INITIAL_EXACT_TIME = "00:00"

private fun List<IqamahConfig>.initialMode(prayerKey: String): String {
    return get(prayerKey)?.mode ?: IqamahMode.AFTER_ADHAN
}

private fun List<IqamahConfig>.initialMinutesAfterAdhan(
    prayerKey: String,
    defaultValue: String
): String {
    return get(prayerKey)?.minutesAfterAdhan?.toString() ?: defaultValue
}

private fun List<IqamahConfig>.initialExactTime(
    prayerKey: String,
    defaultValue: String
): String {
    return get(prayerKey)?.exactTime ?: defaultValue
}
