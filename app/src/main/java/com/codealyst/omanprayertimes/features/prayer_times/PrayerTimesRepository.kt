package com.codealyst.omanprayertimes.features.prayer_times

import com.codealyst.omanprayertimes.features.api.PrayerTimesApiService
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.api.dtos.PrayerTimesByDate
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.entities.DailyPrayerTimesEntity
import com.codealyst.omanprayertimes.features.database.entities.YearlyPrayerTimesEntity
import java.time.LocalDate
import javax.inject.Inject

class PrayerTimesRepository @Inject constructor(
    private val api: PrayerTimesApiService,
    private val yearlyPrayerTimesDao: YearlyPrayerTimesDao,
    private val dailyPrayerTimesDao: DailyPrayerTimesDao,
) {
    companion object {
        private const val PRAYER_TIMES_CACHE_DURATION =
            30 * 24 * 60 * 60 * 1000L // 30 days in milliseconds

        private const val CITIES_CACHE_DURATION =
            30 * 24 * 60 * 60 * 1000L // 30 days in milliseconds
    }

    suspend fun getYearlyPrayerTimes(cityId: Int = 0): PrayerTimesByDate {
        // Prepare the current year
        val currentYear = LocalDate.now().year;
        val now = System.currentTimeMillis();

        // Check the cache for the current year's prayer times.
        val cachedYear = yearlyPrayerTimesDao.getByYear(cityId, currentYear)

        // Return cached prayer times if they haven't expired.
        if (cachedYear != null && cachedYear.expiresAt > now) {
            println("Cached prayer times found for year: $currentYear");
            val cachedPrayerTimes = dailyPrayerTimesDao.getAllByYear(cityId, currentYear)
            return cachedPrayerTimes.associate { it.date to it.toDto() }
        }

        // Refresh prayer times.
        try {
            refreshYearlyPrayerTimes(currentYear, cityId);
        } catch (_: Exception) {
        }

        // Try fetching the prayer times again after refresh.
        yearlyPrayerTimesDao.getByYear(cityId, currentYear)?.let {
            val cachedPrayerTimes = dailyPrayerTimesDao.getAllByYear(cityId, currentYear)
            return cachedPrayerTimes.associate { it.date to it.toDto() }
        }

        // If still not found, try to find the latest cached year and use that as a fallback.
        val fallbackYear =
            yearlyPrayerTimesDao.getLatestCachedYear(cityId)
                ?: error("No prayer times found for any year.");

        val cachedPrayerTimes = dailyPrayerTimesDao.getAllByYear(cityId, fallbackYear)

        return cachedPrayerTimes.associate { it.date to it.toDto() }
    }

    private suspend fun refreshYearlyPrayerTimes(year: Int, cityId: Int) {
        // Fetch prayer times from the API.
        val response = api.getPrayerTimes(year = year, cityId = cityId)

        // Cache the fetched prayer times in the database.
        val fetchedAt = System.currentTimeMillis();
        val expiresAt = fetchedAt + PRAYER_TIMES_CACHE_DURATION;

        yearlyPrayerTimesDao.upsert(
            YearlyPrayerTimesEntity(
                cityId = cityId,
                year = year,
                fetchedAt = fetchedAt,
                expiresAt = expiresAt
            )
        )

        dailyPrayerTimesDao.upsertAll(response.prayerTimes.map { it.value.toEntity(year, cityId) })
    }

    private fun DailyPrayerTimesEntity.toDto(displayDate: String = this.date): DailyPrayerTimes {
        return DailyPrayerTimes(
            date = displayDate,
            fajrTime = this.fajr,
            sunriseTime = this.sunrise,
            dhuhrTime = this.dhuhr,
            asrTime = this.asr,
            maghribTime = this.maghrib,
            ishaTime = this.isha
        )
    }

    private fun DailyPrayerTimes.toEntity(
        year: Int,
        cityId: Int = 0,
        displayDate: String = this.date
    ): DailyPrayerTimesEntity {
        return DailyPrayerTimesEntity(
            year = year,
            cityId = cityId,
            date = displayDate,
            fajr = fajrTime,
            sunrise = sunriseTime,
            dhuhr = dhuhrTime,
            asr = asrTime,
            maghrib = maghribTime,
            isha = ishaTime
        )
    }
}

