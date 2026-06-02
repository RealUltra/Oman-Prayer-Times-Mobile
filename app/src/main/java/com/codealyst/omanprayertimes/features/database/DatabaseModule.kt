package com.codealyst.omanprayertimes.features.database

import android.content.Context
import androidx.room.Room
import com.codealyst.omanprayertimes.features.database.daos.CitiesCacheDao
import com.codealyst.omanprayertimes.features.database.daos.CityDao
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideYearlyPrayerTimesDao(appDatabase: AppDatabase): YearlyPrayerTimesDao =
        appDatabase.getYearlyPrayerTimesDao()

    @Singleton
    @Provides
    fun provideDailyPrayerTimesDao(appDatabase: AppDatabase): DailyPrayerTimesDao =
        appDatabase.getDailyPrayerTimesDao()

    @Singleton
    @Provides
    fun provideCitiesCacheDao(appDatabase: AppDatabase): CitiesCacheDao =
        appDatabase.getCitiesCacheDao()

    @Singleton
    @Provides
    fun provideCityDao(appDatabase: AppDatabase): CityDao =
        appDatabase.getCityDao()

}