package com.codealyst.omanprayertimes.features.database.entities

import androidx.room.Entity

@Entity(tableName = "yearly_prayer_times", primaryKeys = ["year", "cityId"])
data class YearlyPrayerTimesEntity(
    val year: Int,
    val cityId: Int,
    val fetchedAt: Long,
    val expiresAt: Long,
)