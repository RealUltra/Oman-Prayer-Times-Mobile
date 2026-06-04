package com.codealyst.omanprayertimes.features.prayer_times

import com.codealyst.omanprayertimes.features.api.PrayerTimesApiService
import com.codealyst.omanprayertimes.features.api.dtos.City
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.database.daos.CitiesCacheDao
import com.codealyst.omanprayertimes.features.database.daos.CityDao
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.entities.CitiesCacheEntity
import com.codealyst.omanprayertimes.features.database.entities.CityEntity
import com.codealyst.omanprayertimes.features.database.entities.DailyPrayerTimesEntity
import com.codealyst.omanprayertimes.features.database.entities.YearlyPrayerTimesEntity
import com.codealyst.omanprayertimes.features.database.entities.toDto
import java.time.DateTimeException
import java.time.LocalDate
import javax.inject.Inject

class PrayerTimesRepository @Inject constructor(
    private val api: PrayerTimesApiService,
    private val yearlyPrayerTimesDao: YearlyPrayerTimesDao,
    private val dailyPrayerTimesDao: DailyPrayerTimesDao,
    private val citiesCacheDao: CitiesCacheDao,
    private val cityDao: CityDao
) {
    companion object {
        private const val PRAYER_TIMES_CACHE_DURATION =
            30 * 24 * 60 * 60 * 1000L // 30 days in milliseconds

        private const val CITIES_CACHE_DURATION =
            30 * 24 * 60 * 60 * 1000L // 30 days in milliseconds
    }

    suspend fun getCities(cityId: Int? = null): List<City> {
        // Check the cache for cities list.
        val cachedCities = citiesCacheDao.getLatest()

        // Return cached prayer times if they haven't expired.
        val now = System.currentTimeMillis();

        if (cachedCities != null && cachedCities.expiresAt > now) {
            println("Cached cities list found.");
            val cityEntities = cityDao.getByCache(cachedCities.id)
            return cityEntities.map { e -> e.toDto() }
                .filter { c -> cityId == null || c.cityId == cityId };
        }

        // Refresh cities cache.
        try {
            refreshCitiesCache();
        } catch (_: Exception) {
        }

        // Try fetching the cities list again after refresh.
        citiesCacheDao.getLatest()?.let {
            val cityEntities = cityDao.getByCache(it.id)
            return cityEntities.map { e -> e.toDto() }
                .filter { c -> cityId == null || c.cityId == cityId };
        };

        return emptyList();
    }

    suspend fun getPrayerTimesForDate(
        date: LocalDate,
        cityId: Int = 0
    ): DailyPrayerTimes {
        // Prepare the date key with the current year
        val currentYear = LocalDate.now().year;
        val dateKey = date.withYearSafe(currentYear).toString();
        val now = System.currentTimeMillis();

        // Check the cache for prayer times.
        val cachedDay = dailyPrayerTimesDao.getByDate(cityId, dateKey)
        val cachedYear = yearlyPrayerTimesDao.getByYear(cityId, currentYear)

        // Return cached prayer times if they haven't expired.
        if (cachedDay != null && cachedYear != null && cachedYear.expiresAt > now) {
            println("Cached prayer times found for date: ${date.toString()}");
            return cachedDay.toDto(displayDate = date.toString());
        }

        // Refresh prayer times.
        try {
            refreshYearlyPrayerTimes(currentYear, cityId);
        } catch (_: Exception) {
        }

        // Try fetching the prayer times again after refresh.
        dailyPrayerTimesDao.getByDate(cityId, dateKey)?.let {
            return it.toDto(displayDate = date.toString());
        };

        // If still not found, try to find the latest cached year and use that as a fallback.
        val fallbackYear =
            yearlyPrayerTimesDao.getLatestCachedYear(cityId)
                ?: error("No prayer times found for date: ${date.toString()}");

        // Prepare the fallback date key and check the cache again.
        val fallbackDateKey = date.withYearSafe(fallbackYear).toString()
        val fallbackDay = dailyPrayerTimesDao.getByDate(cityId, fallbackDateKey)

        return fallbackDay?.toDto(displayDate = date.toString())
            ?: error("No prayer times found for date: ${date.toString()}");
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

        dailyPrayerTimesDao.upsertAll(
            response.prayerTimes.map { dailyPrayerTimes ->
                DailyPrayerTimesEntity(
                    year = year,
                    cityId = cityId,
                    date = dailyPrayerTimes.value.date,
                    fajr = dailyPrayerTimes.value.fajrTime,
                    sunrise = dailyPrayerTimes.value.shurooqTime,
                    dhuhr = dailyPrayerTimes.value.dhuhrTime,
                    asr = dailyPrayerTimes.value.asrTime,
                    maghrib = dailyPrayerTimes.value.maghribTime,
                    isha = dailyPrayerTimes.value.ishaaTime
                )
            }
        )
    }

    private suspend fun refreshCitiesCache() {
        // Fetch cities list from the API.
        val response = api.getCities()

        // Cache the fetched prayer times in the database.
        val fetchedAt = System.currentTimeMillis();
        val expiresAt = fetchedAt + CITIES_CACHE_DURATION;

        val cacheId = citiesCacheDao.insert(
            CitiesCacheEntity(
                fetchedAt = fetchedAt,
                expiresAt = expiresAt
            )
        ).toInt()

        cityDao.upsertAll(
            response.cities.map { c ->
                CityEntity(
                    cityId = c.cityId,
                    cacheId = cacheId,
                    cityName = c.cityName
                )
            }
        )
    }
}

fun LocalDate.withYearSafe(targetYear: Int): LocalDate {
    return try {
        this.withYear(targetYear)
    } catch (_: DateTimeException) {
        // Handle invalid date (e.g., February 29 on a non-leap year)
        LocalDate.of(targetYear, this.month, this.dayOfMonth.coerceAtMost(28))
    }
}
