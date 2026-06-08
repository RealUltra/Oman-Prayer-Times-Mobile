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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.prayer_times.PrayerKey
import com.codealyst.omanprayertimes.features.prayer_times.titleRes
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PrayerTimesTable(
    prayerTimes: DailyPrayerTimes?,
    iqamahTimes: DailyPrayerTimes?,
    nextEvent: EventInfo,
    tableDate: String,
    modifier: Modifier = Modifier,
) {
    val noTimePlaceholder = stringResource(R.string.no_time_placeholder)
    val fajrTime = prayerTimes?.fajrTime.toDisplayTime(noTimePlaceholder)
    val sunriseTime = prayerTimes?.sunriseTime.toDisplayTime(noTimePlaceholder)
    val dhuhrTime = prayerTimes?.dhuhrTime.toDisplayTime(noTimePlaceholder)
    val asrTime = prayerTimes?.asrTime.toDisplayTime(noTimePlaceholder)
    val maghribTime = prayerTimes?.maghribTime.toDisplayTime(noTimePlaceholder)
    val ishaTime = prayerTimes?.ishaTime.toDisplayTime(noTimePlaceholder)

    val fajrIqamahTime = iqamahTimes?.fajrTime.toDisplayTime(noTimePlaceholder)
    val dhuhrIqamahTime = iqamahTimes?.dhuhrTime.toDisplayTime(noTimePlaceholder)
    val asrIqamahTime = iqamahTimes?.asrTime.toDisplayTime(noTimePlaceholder)
    val maghribIqamahTime = iqamahTimes?.maghribTime.toDisplayTime(noTimePlaceholder)
    val ishaIqamahTime = iqamahTimes?.ishaTime.toDisplayTime(noTimePlaceholder)

    val dateMatches = nextEvent.date == tableDate

    val prayerRowsInfo = listOf(
        Triple(PrayerKey.FAJR, fajrTime, fajrIqamahTime),
        Triple(PrayerKey.SUNRISE, sunriseTime, noTimePlaceholder),
        Triple(PrayerKey.DHUHR, dhuhrTime, dhuhrIqamahTime),
        Triple(PrayerKey.ASR, asrTime, asrIqamahTime),
        Triple(PrayerKey.MAGHRIB, maghribTime, maghribIqamahTime),
        Triple(PrayerKey.ISHA, ishaTime, ishaIqamahTime),
    )

    val colorScheme = MaterialTheme.colorScheme;

    Column(modifier) {
        PrayerTimeRow(
            stringResource(R.string.salah),
            stringResource(R.string.adhan),
            stringResource(R.string.iqamah),
            isHeader = true
        )
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 2.dp)
        Spacer(Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for ((prayerKey, adhanTime, iqamahTime) in prayerRowsInfo) {
                PrayerTimeRow(
                    stringResource(prayerKey.titleRes()),
                    adhanTime,
                    iqamahTime,
                    highlighted = (nextEvent.prayerKey == prayerKey && dateMatches),
                    isAdhanNext = nextEvent.isAdhan
                )
            }
        }
    }
}

private fun String?.toDisplayTime(noTimePlaceholder: String): String {
    return this?.to12HourTime() ?: noTimePlaceholder
}

fun String.to12HourTime(): String {
    return try {
        LocalTime
            .parse(this, DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
            .format(DateTimeFormatter.ofPattern("hh:mm a"))
    } catch (_: Exception) {
        this
    }
}
