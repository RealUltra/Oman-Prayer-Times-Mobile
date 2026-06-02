package com.codealyst.omanprayertimes.features.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codealyst.omanprayertimes.features.database.entities.CityEntity

@Dao
interface CityDao {
    @Query(
        """
        SELECT * FROM cities
        WHERE cacheId = :cacheId
        ORDER BY cityName
        """
    )
    suspend fun getByCache(cacheId: Int): List<CityEntity>

    @Query(
        """
        SELECT * FROM cities
        WHERE cityId = :cityId
        LIMIT 1
        """
    )
    suspend fun getById(cityId: Int): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(cities: List<CityEntity>)
}
