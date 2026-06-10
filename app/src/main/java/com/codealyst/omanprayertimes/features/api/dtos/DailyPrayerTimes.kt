package com.codealyst.omanprayertimes.features.api.dtos

import com.google.gson.annotations.SerializedName

data class DailyPrayerTimes(
    @SerializedName("date")
    val date: String,
    @SerializedName("fajrTime")
    val fajrTime: String,
    @SerializedName("shurooqTime")
    val sunriseTime: String,
    @SerializedName("dhuhrTime")
    val dhuhrTime: String,
    @SerializedName("asrTime")
    val asrTime: String,
    @SerializedName("maghribTime")
    val maghribTime: String,
    @SerializedName("ishaaTime")
    val ishaTime: String,
)
