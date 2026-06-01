package com.codealyst.omanprayertimes.features.api.dtos

data class DailyPrayerTimes(
    val date: String,
    val fajrTime: String,
    val shurooqTime: String,
    val dhuhrTime: String,
    val asrTime: String,
    val maghribTime: String,
    val ishaaTime: String,
)