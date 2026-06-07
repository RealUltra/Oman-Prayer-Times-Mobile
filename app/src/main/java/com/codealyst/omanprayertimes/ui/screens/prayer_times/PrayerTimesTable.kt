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
    val fajrTime = prayerTimes?.fajrTime.toDisplayTime()
    val sunriseTime = prayerTimes?.sunriseTime.toDisplayTime()
    val dhuhrTime = prayerTimes?.dhuhrTime.toDisplayTime()
    val asrTime = prayerTimes?.asrTime.toDisplayTime()
    val maghribTime = prayerTimes?.maghribTime.toDisplayTime()
    val ishaTime = prayerTimes?.ishaTime.toDisplayTime()

    val fajrIqamahTime = iqamahTimes?.fajrTime.toDisplayTime()
    val dhuhrIqamahTime = iqamahTimes?.dhuhrTime.toDisplayTime()
    val asrIqamahTime = iqamahTimes?.asrTime.toDisplayTime()
    val maghribIqamahTime = iqamahTimes?.maghribTime.toDisplayTime()
    val ishaIqamahTime = iqamahTimes?.ishaTime.toDisplayTime()

    val dateMatches = nextEvent.date == tableDate

    val prayerRowsInfo = listOf(
        Triple("Fajr", fajrTime, fajrIqamahTime),
        Triple("Shurooq", sunriseTime, "-"),
        Triple("Dhuhr", dhuhrTime, dhuhrIqamahTime),
        Triple("Asr", asrTime, asrIqamahTime),
        Triple("Maghrib", maghribTime, maghribIqamahTime),
        Triple("Isha'a", ishaTime, ishaIqamahTime),
    )

    val colorScheme = MaterialTheme.colorScheme;

    Column(modifier) {
        PrayerTimeRow("Salah", "Adhan", "Iqamah", isHeader = true)
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 2.dp)
        Spacer(Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for ((salahName, adhanTime, iqamahTime) in prayerRowsInfo) {
                PrayerTimeRow(
                    salahName,
                    adhanTime,
                    iqamahTime,
                    highlighted = (nextEvent.salahName == salahName && dateMatches),
                    isAdhanNext = nextEvent.isAdhan
                )
            }
        }
    }
}

private fun String?.toDisplayTime(): String {
    return this?.to12HourTime() ?: "-"
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
