package com.codealyst.omanprayertimes.features.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codealyst.omanprayertimes.features.database.entities.DailyPrayerTimesEntity

@Dao
interface DailyPrayerTimesDao {
    @Query(
        """
        SELECT * FROM daily_prayer_times
        WHERE cityId = :cityId AND date = :date
        LIMIT 1
        """
    )
    suspend fun getByDate(cityId: Int, date: String): DailyPrayerTimesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(prayerTimes: List<DailyPrayerTimesEntity>)
}
