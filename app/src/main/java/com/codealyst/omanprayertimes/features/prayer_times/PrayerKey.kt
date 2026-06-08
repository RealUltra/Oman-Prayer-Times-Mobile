package com.codealyst.omanprayertimes.features.prayer_times

import androidx.annotation.StringRes
import com.codealyst.omanprayertimes.R

enum class PrayerKey {
    FAJR,
    SUNRISE,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA
}

@StringRes
fun PrayerKey.titleRes(): Int = when (this) {
    PrayerKey.FAJR -> R.string.fajr
    PrayerKey.SUNRISE -> R.string.sunrise
    PrayerKey.DHUHR -> R.string.dhuhr
    PrayerKey.ASR -> R.string.asr
    PrayerKey.MAGHRIB -> R.string.maghrib
    PrayerKey.ISHA -> R.string.isha
}