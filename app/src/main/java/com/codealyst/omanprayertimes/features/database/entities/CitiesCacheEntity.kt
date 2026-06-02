package com.codealyst.omanprayertimes.features.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities_cache")
data class CitiesCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fetchedAt: Long,
    val expiresAt: Long,
)
