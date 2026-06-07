package com.codealyst.omanprayertimes.features.api.dtos

import com.google.gson.annotations.SerializedName

data class DailyPrayerTimes(
    val date: String,
    val fajrTime: String,
    @SerializedName("shurooqTime")
    val sunriseTime: String,
    val dhuhrTime: String,
    val asrTime: String,
    val maghribTime: String,
    @SerializedName("ishaaTime")
    val ishaTime: String,
)