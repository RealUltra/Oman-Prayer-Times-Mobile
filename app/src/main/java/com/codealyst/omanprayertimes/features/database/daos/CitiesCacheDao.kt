package com.codealyst.omanprayertimes.features.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codealyst.omanprayertimes.features.database.entities.CitiesCacheEntity

@Dao
interface CitiesCacheDao {
    @Query(
        """
        SELECT * FROM cities_cache
        ORDER BY fetchedAt DESC
        LIMIT 1
        """
    )
    suspend fun getLatest(): CitiesCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cache: CitiesCacheEntity): Long
}
