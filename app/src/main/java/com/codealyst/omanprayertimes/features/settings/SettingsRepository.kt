package com.codealyst.omanprayertimes.features.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.codealyst.omanprayertimes.features.database.daos.IqamahConfigDao
import com.codealyst.omanprayertimes.features.database.entities.IqamahConfigEntity
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import com.codealyst.omanprayertimes.features.settings.dtos.PrayerKeys
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
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme")
        val IQAMAH_TIMES_ENABLED = booleanPreferencesKey("iqamah_times_enabled")
        val DEFAULT_IQAMAH_CONFIGS = listOf(
            IqamahConfig(PrayerKeys.FAJR, IqamahMode.AFTER_ADHAN, 25, null),
            IqamahConfig(PrayerKeys.DHUHR, IqamahMode.AFTER_ADHAN, 20, null),
            IqamahConfig(PrayerKeys.ASR, IqamahMode.AFTER_ADHAN, 20, null),
            IqamahConfig(PrayerKeys.MAGHRIB, IqamahMode.AFTER_ADHAN, 5, null),
            IqamahConfig(PrayerKeys.ISHA, IqamahMode.AFTER_ADHAN, 20, null)
        )
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            cityId = prefs[CITY_ID] ?: 0,
            language = prefs[LANGUAGE] ?: "",
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

    suspend fun setLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE] = language
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
            prayerKey = prayerKey,
            mode = mode,
            minutesAfterAdhan = minutesAfterAdhan,
            exactTime = exactTime
        )
    }

    private fun IqamahConfig.toEntity(): IqamahConfigEntity {
        return IqamahConfigEntity(
            prayerKey = prayerKey,
            mode = mode,
            minutesAfterAdhan = minutesAfterAdhan,
            exactTime = exactTime
        )
    }
}
