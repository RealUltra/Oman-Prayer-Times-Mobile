package com.codealyst.omanprayertimes.features.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val CITY_ID = intPreferencesKey("city_id")
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme")
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            cityId = prefs[CITY_ID] ?: 0,
            language = prefs[LANGUAGE] ?: "",
            theme = prefs[THEME] ?: ""
        )
    }

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
}
