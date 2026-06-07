package com.codealyst.omanprayertimes.features.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "iqamah_configs")
data class IqamahConfigEntity(
    @PrimaryKey
    val prayerKey: String, // "fajr", "dhuhr", "asr", "maghrib", "isha"
    val mode: String,  // "exact_time", "after_adhan"
    val minutesAfterAdhan: Int?,
    val exactTime: String?
)