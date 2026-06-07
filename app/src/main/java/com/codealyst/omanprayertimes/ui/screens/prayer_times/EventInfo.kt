package com.codealyst.omanprayertimes.ui.screens.prayer_times

import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class EventInfo(
    val salahName: String = "",
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
        Triple("Fajr", todayPrayerTimes.fajrTime, iqamahTimes?.fajrTime),
        Triple("Shurooq", todayPrayerTimes.sunriseTime, null),
        Triple("Dhuhr", todayPrayerTimes.dhuhrTime, iqamahTimes?.dhuhrTime),
        Triple("Asr", todayPrayerTimes.asrTime, iqamahTimes?.asrTime),
        Triple("Maghrib", todayPrayerTimes.maghribTime, iqamahTimes?.maghribTime),
        Triple("Isha'a", todayPrayerTimes.ishaTime, iqamahTimes?.ishaTime),
    )

    val currentTime = now.toLocalTime();

    // Prepare variables for next event
    var nextSalahName = ""
    var nextEventTime: LocalTime? = null
    var nextEventIsAdhan = true

    for ((salahName, adhanTimeStr, iqamahTimeStr) in orderedEventTimes) {
        val adhanTime = adhanTimeStr.toLocalTime()
        val iqamahTime = iqamahTimeStr?.nullIfDash()?.toLocalTime()

        if (adhanTime.isAfter(currentTime)) {
            nextSalahName = salahName
            nextEventTime = adhanTime
            nextEventIsAdhan = true
            break
        }

        if (iqamahTime != null && iqamahTime.isAfter(currentTime)) {
            nextSalahName = salahName
            nextEventTime = iqamahTime
            nextEventIsAdhan = false
            break
        }
    }

    val nextEventDate = now.toLocalDate();

    if (nextEventTime == null) {
        nextSalahName = "Fajr"
        nextEventTime = tomorrowPrayerTimes.fajrTime.toLocalTime()
        nextEventDate.plusDays(1)
    }

    val nextEventDateTime = nextEventDate.atTime(nextEventTime).atZone(now.zone)
    val millisecondsLeft = Duration.between(now, nextEventDateTime).toMillis()
    val secondsLeft = ((millisecondsLeft + 999) / 1000).toInt()

    return EventInfo(
        salahName = nextSalahName,
        isAdhan = nextEventIsAdhan,
        isShurooq = nextSalahName == "Shurooq",
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