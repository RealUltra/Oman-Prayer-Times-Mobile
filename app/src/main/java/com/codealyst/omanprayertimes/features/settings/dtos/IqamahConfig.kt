package com.codealyst.omanprayertimes.features.settings.dtos

import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class IqamahConfig(
    val prayerKey: String,
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

fun List<IqamahConfig>.get(prayerKey: String): IqamahConfig? {
    return this.firstOrNull { config -> config.prayerKey == prayerKey }
}

fun List<IqamahConfig>.getIqamahTime(prayerKey: String, adhanTime: String?): String? {
    return this.get(prayerKey)?.getIqamahTime(adhanTime)
}

object IqamahMode {
    const val EXACT_TIME = "exact_time"
    const val AFTER_ADHAN = "after_adhan"
}

object PrayerKeys {
    const val FAJR = "fajr"
    const val DHUHR = "dhuhr"
    const val ASR = "asr"
    const val MAGHRIB = "maghrib"
    const val ISHA = "isha"
}