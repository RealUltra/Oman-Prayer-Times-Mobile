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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.oman_datetime.getOmanDate
import com.codealyst.omanprayertimes.features.prayer_times.PrayerKey
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.PrayerTimesViewModel
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.UiState
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import com.codealyst.omanprayertimes.features.settings.dtos.get
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.ScreenHeader

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

    LaunchedEffect(settings.cityId) {
        prayerTimesViewModel.fetchPrayerTimesForDate(
            getOmanDate(),
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
        ScreenHeader(title = stringResource(R.string.iqamah_times))

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
                    prayerKey = PrayerKey.FAJR,
                    initialMode = iqamahConfigs.initialMode(PrayerKey.FAJR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKey.FAJR,
                        "25"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKey.FAJR,
                        prayerTimes?.fajrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKey.DHUHR,
                    initialMode = iqamahConfigs.initialMode(PrayerKey.DHUHR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKey.DHUHR,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKey.DHUHR,
                        prayerTimes?.dhuhrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKey.ASR,
                    initialMode = iqamahConfigs.initialMode(PrayerKey.ASR),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKey.ASR,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKey.ASR,
                        prayerTimes?.asrTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKey.MAGHRIB,
                    initialMode = iqamahConfigs.initialMode(PrayerKey.MAGHRIB),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKey.MAGHRIB,
                        "5"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKey.MAGHRIB,
                        prayerTimes?.maghribTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
                IqamahConfigCard(
                    prayerKey = PrayerKey.ISHA,
                    initialMode = iqamahConfigs.initialMode(PrayerKey.ISHA),
                    initialMinutesAfterAdhan = iqamahConfigs.initialMinutesAfterAdhan(
                        PrayerKey.ISHA,
                        "20"
                    ),
                    initialExactTime = iqamahConfigs.initialExactTime(
                        PrayerKey.ISHA,
                        prayerTimes?.ishaTime ?: DEFAULT_INITIAL_EXACT_TIME
                    ),
                    onChanged = settingsViewModel::setIqamahConfig
                )
            }
        }
    }
}

private const val DEFAULT_INITIAL_EXACT_TIME = "00:00"

private fun List<IqamahConfig>.initialMode(prayerKey: PrayerKey): String {
    return get(prayerKey)?.mode ?: IqamahMode.AFTER_ADHAN
}

private fun List<IqamahConfig>.initialMinutesAfterAdhan(
    prayerKey: PrayerKey,
    defaultValue: String
): String {
    return get(prayerKey)?.minutesAfterAdhan?.toString() ?: defaultValue
}

private fun List<IqamahConfig>.initialExactTime(
    prayerKey: PrayerKey,
    defaultValue: String
): String {
    return get(prayerKey)?.exactTime ?: defaultValue
}
