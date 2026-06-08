package com.codealyst.omanprayertimes.features.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.codealyst.omanprayertimes.features.database.daos.IqamahConfigDao
import com.codealyst.omanprayertimes.features.database.entities.IqamahConfigEntity
import com.codealyst.omanprayertimes.features.prayer_times.PrayerKey
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val iqamahConfigDao: IqamahConfigDao
) {
    private companion object {
        val CITY_ID = intPreferencesKey("city_id")
        val THEME = stringPreferencesKey("theme")
        val IQAMAH_TIMES_ENABLED = booleanPreferencesKey("iqamah_times_enabled")
        val DEFAULT_IQAMAH_CONFIGS = listOf(
            IqamahConfig(PrayerKey.FAJR, IqamahMode.AFTER_ADHAN, 25, null),
            IqamahConfig(PrayerKey.DHUHR, IqamahMode.AFTER_ADHAN, 20, null),
            IqamahConfig(PrayerKey.ASR, IqamahMode.AFTER_ADHAN, 20, null),
            IqamahConfig(PrayerKey.MAGHRIB, IqamahMode.AFTER_ADHAN, 5, null),
            IqamahConfig(PrayerKey.ISHA, IqamahMode.AFTER_ADHAN, 20, null)
        )
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            cityId = prefs[CITY_ID] ?: 0,
            theme = prefs[THEME] ?: "",
            iqamahTimesEnabled = prefs[IQAMAH_TIMES_ENABLED] ?: false
        )
    }

    val iqamahConfigsFlow: Flow<List<IqamahConfig>> = iqamahConfigDao.observeAll()
        .onStart { ensureIqamahConfigsExist() }
        .map { configs -> configs.map { it.toDto() } }

    suspend fun setCityId(cityId: Int) {
        dataStore.edit { prefs ->
            prefs[CITY_ID] = cityId
        }
    }

    suspend fun setTheme(theme: String) {
        dataStore.edit { prefs ->
            prefs[THEME] = theme
        }
    }

    suspend fun setIqamahTimesEnabled(iqamahTimesEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[IQAMAH_TIMES_ENABLED] = iqamahTimesEnabled
        }
    }

    suspend fun setIqamahConfig(config: IqamahConfig) {
        iqamahConfigDao.upsert(config.toEntity())
    }

    private suspend fun ensureIqamahConfigsExist() {
        if (iqamahConfigDao.count() == 0) {
            iqamahConfigDao.upsertAll(DEFAULT_IQAMAH_CONFIGS.map { it.toEntity() })
        }
    }

    private fun IqamahConfigEntity.toDto(): IqamahConfig {
        return IqamahConfig(
            prayerKey = prayerKey.toPrayerKey(),
            mode = mode,
            minutesAfterAdhan = minutesAfterAdhan,
            exactTime = exactTime
        )
    }

    private fun IqamahConfig.toEntity(): IqamahConfigEntity {
        return IqamahConfigEntity(
            prayerKey = prayerKey.toIqamahConfigKey(),
            mode = mode,
            minutesAfterAdhan = minutesAfterAdhan,
            exactTime = exactTime
        )
    }

    private fun PrayerKey.toIqamahConfigKey(): String = when (this) {
        PrayerKey.FAJR -> "fajr"
        PrayerKey.SUNRISE -> "sunrise"
        PrayerKey.DHUHR -> "dhuhr"
        PrayerKey.ASR -> "asr"
        PrayerKey.MAGHRIB -> "maghrib"
        PrayerKey.ISHA -> "isha"
    }

    private fun String.toPrayerKey(): PrayerKey = when (this) {
        "fajr" -> PrayerKey.FAJR
        "dhuhr" -> PrayerKey.DHUHR
        "asr" -> PrayerKey.ASR
        "maghrib" -> PrayerKey.MAGHRIB
        "isha" -> PrayerKey.ISHA
        else -> PrayerKey.SUNRISE
    }
}
