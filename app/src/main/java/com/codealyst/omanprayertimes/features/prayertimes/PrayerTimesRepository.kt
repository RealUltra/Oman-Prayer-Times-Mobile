package com.codealyst.omanprayertimes.features.prayertimes

import com.codealyst.omanprayertimes.features.api.RetrofitInstance
import com.codealyst.omanprayertimes.features.api.dtos.City
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.api.dtos.PrayerTimesPayload
import java.time.LocalDate

class PrayerTimesRepository {

    suspend fun getCities(cityId: Int? = null): List<City> {
        return RetrofitInstance.api.getCities(cityId).cities;
    }

    suspend fun getPrayerTimes(
        year: Int? = null,
        month: Int? = null,
        cityId: Int? = null
    ): PrayerTimesPayload {
        return RetrofitInstance.api.getPrayerTimes(year, month, cityId);
    }

    suspend fun getPrayerTimesForDate(date: LocalDate, cityId: Int? = null): DailyPrayerTimes {
        val payload = RetrofitInstance.api.getPrayerTimes(
            year = date.year,
            month = date.month.value,
            cityId = cityId,
        )

        return payload.prayerTimes[date.toString()]
            ?: error("No prayer times found for date: ${date.toString()}");
    }
}