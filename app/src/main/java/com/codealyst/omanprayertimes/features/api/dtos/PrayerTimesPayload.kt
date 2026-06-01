package com.codealyst.omanprayertimes.features.api.dtos

data class PrayerTimesPayload(
    val cityId: Int,
    val cityName: String,
    val prayerTimes: PrayerTimesByDate
)