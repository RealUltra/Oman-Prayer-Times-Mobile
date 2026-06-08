package com.codealyst.omanprayertimes.ui.screens.prayer_times

import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.prayer_times.PrayerKey
import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class EventInfo(
    val prayerKey: PrayerKey = PrayerKey.FAJR,
    val isAdhan: Boolean = false,
    val isShurooq: Boolean = false,
    val secondsLeft: Int = 0,
    val date: String = ""
)

fun getNextEvent(
    now: ZonedDateTime,
    todayPrayerTimes: DailyPrayerTimes,
    tomorrowPrayerTimes: DailyPrayerTimes,
    iqamahTimes: DailyPrayerTimes?
): EventInfo {
    // List of triple tuples: (Salah Name, Adhan Time, Iqamah Time)
    val orderedEventTimes = listOf(
        Triple(PrayerKey.FAJR, todayPrayerTimes.fajrTime, iqamahTimes?.fajrTime),
        Triple(PrayerKey.SUNRISE, todayPrayerTimes.sunriseTime, null),
        Triple(PrayerKey.DHUHR, todayPrayerTimes.dhuhrTime, iqamahTimes?.dhuhrTime),
        Triple(PrayerKey.ASR, todayPrayerTimes.asrTime, iqamahTimes?.asrTime),
        Triple(PrayerKey.MAGHRIB, todayPrayerTimes.maghribTime, iqamahTimes?.maghribTime),
        Triple(PrayerKey.ISHA, todayPrayerTimes.ishaTime, iqamahTimes?.ishaTime),
    )

    val currentTime = now.toLocalTime();

    // Prepare variables for next event
    var nextPrayerKey: PrayerKey = PrayerKey.FAJR
    var nextEventTime: LocalTime? = null
    var nextEventIsAdhan = true

    for ((prayerKey, adhanTimeStr, iqamahTimeStr) in orderedEventTimes) {
        val adhanTime = adhanTimeStr.toLocalTime()
        val iqamahTime = iqamahTimeStr?.nullIfDash()?.toLocalTime()

        if (adhanTime.isAfter(currentTime)) {
            nextPrayerKey = prayerKey
            nextEventTime = adhanTime
            nextEventIsAdhan = true
            break
        }

        if (iqamahTime != null && iqamahTime.isAfter(currentTime)) {
            nextPrayerKey = prayerKey
            nextEventTime = iqamahTime
            nextEventIsAdhan = false
            break
        }
    }

    var nextEventDate = now.toLocalDate();

    if (nextEventTime == null) {
        nextEventTime = tomorrowPrayerTimes.fajrTime.toLocalTime()
        nextEventDate = nextEventDate.plusDays(1)
    }

    val nextEventDateTime = nextEventDate.atTime(nextEventTime).atZone(now.zone)
    val millisecondsLeft = Duration.between(now, nextEventDateTime).toMillis()
    val secondsLeft = ((millisecondsLeft + 999) / 1000).toInt()

    return EventInfo(
        prayerKey = nextPrayerKey,
        isAdhan = nextEventIsAdhan,
        isShurooq = nextPrayerKey == PrayerKey.SUNRISE,
        secondsLeft = secondsLeft,
        date = nextEventDate.toString()
    )
}

private fun String.toLocalTime(): LocalTime {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this, timeFormatter)
}

private fun String.nullIfDash(): String? {
    return if (this == "-") null else this
}