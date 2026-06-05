package com.codealyst.omanprayertimes.features.settings

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val gson = Gson()

data class IqamahSetting(
    val prayerKey: String,
    val mode: String,
    val minutesAfterAdhan: Int?,
    val exactTime: String?
)

fun List<IqamahSetting>.toJson(): String {
    return gson.toJson(this);
}

fun String.toIqamahSettings(): List<IqamahSetting> {
    val type = object : TypeToken<List<IqamahSetting>>() {}.type
    return gson.fromJson(this, type)
}

fun IqamahSetting.getIqamahTime(adhanTime: String?): String? {
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

fun List<IqamahSetting>.get(prayerKey: String): IqamahSetting? {
    for (iqamahSetting in this) {
        if (iqamahSetting.prayerKey == prayerKey) {
            return iqamahSetting;
        }
    }
    return null;
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