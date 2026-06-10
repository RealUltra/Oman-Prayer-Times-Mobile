package com.codealyst.omanprayertimes.features.api.dtos

import com.google.gson.annotations.SerializedName

data class PrayerTimesPayload(
    @SerializedName("cityId")
    val cityId: Int,
    @SerializedName("cityName")
    val cityName: String,
    @SerializedName("prayerTimes")
    val prayerTimes: PrayerTimesByDate
)
