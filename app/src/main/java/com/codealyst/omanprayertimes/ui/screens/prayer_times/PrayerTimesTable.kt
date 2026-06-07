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
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.PrayerKeys
import com.codealyst.omanprayertimes.features.settings.dtos.getIqamahTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PrayerTimesTable(
    prayerTimes: DailyPrayerTimes?,
    iqamahConfigs: List<IqamahConfig>,
    timerMetadata: TimerMetadata,
    modifier: Modifier = Modifier,
) {
    val fajrTime = prayerTimes?.fajrTime.toDisplayTime()
    val shurooqTime = prayerTimes?.shurooqTime.toDisplayTime()
    val dhuhrTime = prayerTimes?.dhuhrTime.toDisplayTime()
    val asrTime = prayerTimes?.asrTime.toDisplayTime()
    val maghribTime = prayerTimes?.maghribTime.toDisplayTime()
    val ishaTime = prayerTimes?.ishaaTime.toDisplayTime()

    val fajrIqamahTime = iqamahConfigs.getIqamahDisplayTime(PrayerKeys.FAJR, prayerTimes)
    val dhuhrIqamahTime = iqamahConfigs.getIqamahDisplayTime(PrayerKeys.DHUHR, prayerTimes)
    val asrIqamahTime = iqamahConfigs.getIqamahDisplayTime(PrayerKeys.ASR, prayerTimes)
    val maghribIqamahTime = iqamahConfigs.getIqamahDisplayTime(PrayerKeys.MAGHRIB, prayerTimes)
    val ishaIqamahTime = iqamahConfigs.getIqamahDisplayTime(PrayerKeys.ISHA, prayerTimes)

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

private fun String?.toDisplayTime(): String {
    return this?.to12HourTime() ?: "-"
}

private fun List<IqamahConfig>.getIqamahDisplayTime(
    prayerKey: String,
    prayerTimes: DailyPrayerTimes?
): String {
    val adhanTime = when (prayerKey) {
        PrayerKeys.FAJR -> prayerTimes?.fajrTime
        PrayerKeys.DHUHR -> prayerTimes?.dhuhrTime
        PrayerKeys.ASR -> prayerTimes?.asrTime
        PrayerKeys.MAGHRIB -> prayerTimes?.maghribTime
        PrayerKeys.ISHA -> prayerTimes?.ishaaTime
        else -> null
    }

    return getIqamahTime(prayerKey, adhanTime).toDisplayTime()
}

fun String.to12HourTime(): String {
    return try {
        LocalTime
            .parse(this, DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
            .format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    } catch (_: Exception) {
        this
    }
}
