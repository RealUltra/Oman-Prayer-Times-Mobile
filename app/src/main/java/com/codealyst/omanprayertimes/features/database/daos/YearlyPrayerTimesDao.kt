package com.codealyst.omanprayertimes.features.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codealyst.omanprayertimes.features.database.entities.YearlyPrayerTimesEntity

@Dao
interface YearlyPrayerTimesDao {
    @Query(
        """
        SELECT * FROM yearly_prayer_times
        WHERE cityId = :cityId AND year = :year
        LIMIT 1
        """
    )
    suspend fun getByYear(cityId: Int, year: Int): YearlyPrayerTimesEntity?

    @Query(
        """
        SELECT MAX(year) FROM yearly_prayer_times
        WHERE cityId = :cityId
        """
    )
    suspend fun getLatestCachedYear(cityId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(yearlyPrayerTimes: YearlyPrayerTimesEntity)
}
