package com.codealyst.omanprayertimes.features.api

import com.codealyst.omanprayertimes.features.api.dtos.CitiesPayload
import com.codealyst.omanprayertimes.features.api.dtos.PrayerTimesPayload
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerTimesApiService {
    @GET("prayer-times")
    suspend fun getPrayerTimes(
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null,
        @Query("cityId") cityId: Int? = null
    ): PrayerTimesPayload

    @GET("cities")
    suspend fun getCities(@Query("cityId") cityId: Int? = null): CitiesPayload
}