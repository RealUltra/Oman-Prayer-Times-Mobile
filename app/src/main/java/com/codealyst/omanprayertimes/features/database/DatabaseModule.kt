package com.codealyst.omanprayertimes.features.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codealyst.omanprayertimes.features.database.daos.DailyPrayerTimesDao
import com.codealyst.omanprayertimes.features.database.daos.IqamahConfigDao
import com.codealyst.omanprayertimes.features.database.daos.YearlyPrayerTimesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Singleton
    @Provides
    fun provideYearlyPrayerTimesDao(appDatabase: AppDatabase): YearlyPrayerTimesDao =
        appDatabase.getYearlyPrayerTimesDao()

    @Singleton
    @Provides
    fun provideDailyPrayerTimesDao(appDatabase: AppDatabase): DailyPrayerTimesDao =
        appDatabase.getDailyPrayerTimesDao()

    @Singleton
    @Provides
    fun provideIqamahConfigDao(appDatabase: AppDatabase): IqamahConfigDao =
        appDatabase.getIqamahConfigDao()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS iqamah_configs (
                    prayerKey TEXT NOT NULL PRIMARY KEY,
                    mode TEXT NOT NULL,
                    minutesAfterAdhan INTEGER,
                    exactTime TEXT
                )
                """.trimIndent()
            )
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS cities")
            db.execSQL("DROP TABLE IF EXISTS cities_cache")
        }
    }
}
