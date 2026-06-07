package com.codealyst.omanprayertimes.features.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codealyst.omanprayertimes.features.database.entities.IqamahConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IqamahConfigDao {
    @Query(
        """
        SELECT * FROM iqamah_configs
        ORDER BY CASE prayerKey
            WHEN 'fajr' THEN 1
            WHEN 'dhuhr' THEN 2
            WHEN 'asr' THEN 3
            WHEN 'maghrib' THEN 4
            WHEN 'isha' THEN 5
            ELSE 6
        END
        """
    )
    fun observeAll(): Flow<List<IqamahConfigEntity>>

    @Query("SELECT COUNT(*) FROM iqamah_configs")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(config: IqamahConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(configs: List<IqamahConfigEntity>)
}
