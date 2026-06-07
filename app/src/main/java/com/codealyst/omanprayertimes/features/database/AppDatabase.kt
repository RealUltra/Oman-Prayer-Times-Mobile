package com.codealyst.omanprayertimes.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codealyst.omanprayertimes.features.database.daos.CitiesCacheDao
import com.codealyst.omanprayertimes.features.database.daos.CityDao
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.IqamahConfigDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.entities.CitiesCacheEntity
import com.codealyst.omanprayertimes.features.database.entities.CityEntity
import com.codealyst.omanprayertimes.features.database.entities.DailyPrayerTimesEntity
import com.codealyst.omanprayertimes.features.database.entities.IqamahConfigEntity
import com.codealyst.omanprayertimes.features.database.entities.YearlyPrayerTimesEntity

@Database(
    version = 2,
    entities = [
        YearlyPrayerTimesEntity::class,
        DailyPrayerTimesEntity::class,
        CitiesCacheEntity::class,
        CityEntity::class,
        IqamahConfigEntity::class
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getYearlyPrayerTimesDao(): YearlyPrayerTimesDao
    abstract fun getDailyPrayerTimesDao(): DailyPrayerTimesDao
    abstract fun getCitiesCacheDao(): CitiesCacheDao
    abstract fun getCityDao(): CityDao
    abstract fun getIqamahConfigDao(): IqamahConfigDao
}
