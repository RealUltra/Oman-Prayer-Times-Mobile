package com.codealyst.omanprayertimes.features.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.codealyst.omanprayertimes.features.api.dtos.City

@Entity(
    tableName = "cities",
    foreignKeys = [ForeignKey(
        entity = CitiesCacheEntity::class,
        parentColumns = ["id"],
        childColumns = ["cacheId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CityEntity(
    @PrimaryKey
    val cityId: Int,
    val cacheId: Int,
    val cityName: String
)

fun CityEntity.toDto(): City {
    return City(cityId = cityId, cityName = cityName)
}