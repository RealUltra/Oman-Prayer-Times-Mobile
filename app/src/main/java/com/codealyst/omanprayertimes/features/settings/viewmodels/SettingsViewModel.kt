package com.codealyst.omanprayertimes.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codealyst.omanprayertimes.features.settings.AppSettings
import com.codealyst.omanprayertimes.features.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    ViewModel() {
    val settings: StateFlow<AppSettings> = repository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    fun setCityId(cityId: Int) {
        viewModelScope.launch {
            repository.setCityId(cityId)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }
}