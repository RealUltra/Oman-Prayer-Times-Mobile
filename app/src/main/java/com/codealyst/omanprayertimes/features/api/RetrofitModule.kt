package com.codealyst.omanprayertimes.features.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val API_URL = "https://oman-prayer-times-api.mirzarameezahmedbaig.workers.dev/api/v1/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePrayerTimesApiService(
        retrofit: Retrofit
    ): PrayerTimesApiService {
        return retrofit.create(PrayerTimesApiService::class.java)
    }
}