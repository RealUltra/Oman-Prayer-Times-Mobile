package com.codealyst.omanprayertimes.features.prayertimes

import com.codealyst.omanprayertimes.features.api.PrayerTimesApiService
import com.codealyst.omanprayertimes.features.api.dtos.City
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.database.daos.CitiesCacheDao
import com.codealyst.omanprayertimes.features.database.daos.CityDao
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import java.time.LocalDate
import javax.inject.Inject

class PrayerTimesRepository @Inject constructor(
    private val api: PrayerTimesApiService,
    private val yearlyPrayerTimesDao: YearlyPrayerTimesDao,
    private val dailyPrayerTimesDao: DailyPrayerTimesDao,
    private val citiesCacheDao: CitiesCacheDao,
    private val cityDao: CityDao
) {
    suspend fun getCities(cityId: Int? = null): List<City> {
        return api.getCities(cityId).cities;
    }

    suspend fun getPrayerTimesForDate(date: LocalDate, cityId: Int? = null): DailyPrayerTimes {
        val payload = api.getPrayerTimes(
            year = date.year,
            month = date.month.value,
            cityId = cityId,
        )

        return payload.prayerTimes[date.toString()]
            ?: error("No prayer times found for date: ${date.toString()}");
    }
}