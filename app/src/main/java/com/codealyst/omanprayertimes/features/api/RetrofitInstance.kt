package com.codealyst.omanprayertimes.features.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL =
        "https://oman-prayer-times-api.mirzarameezahmedbaig.workers.dev/api/v1/"

    val api: PrayerTimesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimesApiService::class.java)
    }
}