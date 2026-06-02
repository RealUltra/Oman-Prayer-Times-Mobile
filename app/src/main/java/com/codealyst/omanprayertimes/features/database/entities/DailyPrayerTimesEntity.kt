package com.codealyst.omanprayertimes.features.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes

@Entity(
    tableName = "daily_prayer_times", primaryKeys = ["date", "cityId"], foreignKeys = [
        ForeignKey(
            entity = YearlyPrayerTimesEntity::class,
            parentColumns = ["year", "cityId"],
            childColumns = ["year", "cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyPrayerTimesEntity(
    val date: String,
    val cityId: Int,
    val year: Int,

    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

fun DailyPrayerTimesEntity.toDto(displayDate: String = this.date): DailyPrayerTimes {
    return DailyPrayerTimes(
        date = displayDate,
        fajrTime = this.fajr,
        shurooqTime = this.sunrise,
        dhuhrTime = this.dhuhr,
        asrTime = this.asr,
        maghribTime = this.maghrib,
        ishaaTime = this.isha
    )
}
