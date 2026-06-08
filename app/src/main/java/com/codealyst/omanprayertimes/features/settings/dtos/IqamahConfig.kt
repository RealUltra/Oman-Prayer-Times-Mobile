package com.codealyst.omanprayertimes.features.settings.dtos

import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.prayer_times.PrayerKey
import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class IqamahConfig(
    val prayerKey: PrayerKey,
    val mode: String,
    val minutesAfterAdhan: Int?,
    val exactTime: String?
)

fun IqamahConfig.getIqamahTime(adhanTime: String?): String? {
    if (mode == IqamahMode.EXACT_TIME) {
        return exactTime
    }

    if (mode == IqamahMode.AFTER_ADHAN && adhanTime != null && minutesAfterAdhan != null) {
        return LocalTime
            .parse(adhanTime, DateTimeFormatter.ofPattern("HH:mm"))
            .plusMinutes(minutesAfterAdhan.toLong())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    return null
}

fun List<IqamahConfig>.get(prayerKey: PrayerKey): IqamahConfig? {
    return this.firstOrNull { config -> config.prayerKey == prayerKey }
}

fun List<IqamahConfig>.getIqamahTime(prayerKey: PrayerKey, adhanTime: String?): String? {
    return get(prayerKey)?.getIqamahTime(adhanTime)
}

fun List<IqamahConfig>.getIqamahTimes(prayerTimes: DailyPrayerTimes): DailyPrayerTimes {
    val fajrIqamahTime = this.getIqamahTime(PrayerKey.FAJR, prayerTimes.fajrTime)
    val dhuhrIqamahTime = this.getIqamahTime(PrayerKey.DHUHR, prayerTimes.dhuhrTime)
    val asrIqamahTime = this.getIqamahTime(PrayerKey.ASR, prayerTimes.asrTime)
    val maghribIqamahTime = this.getIqamahTime(PrayerKey.MAGHRIB, prayerTimes.maghribTime)
    val ishaIqamahTime = this.getIqamahTime(PrayerKey.ISHA, prayerTimes.ishaTime)

    return DailyPrayerTimes(
        date = prayerTimes.date,
        fajrTime = fajrIqamahTime ?: "-",
        sunriseTime = "-",
        dhuhrTime = dhuhrIqamahTime ?: "-",
        asrTime = asrIqamahTime ?: "-",
        maghribTime = maghribIqamahTime ?: "-",
        ishaTime = ishaIqamahTime ?: "-"
    )
}

object IqamahMode {
    const val EXACT_TIME = "exact_time"
    const val AFTER_ADHAN = "after_adhan"
}