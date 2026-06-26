package com.codealyst.omanprayertimes.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codealyst.omanprayertimes.features.settings.AppSettings
import com.codealyst.omanprayertimes.features.settings.SettingsRepository
import com.codealyst.omanprayertimes.features.settings.SetupStep
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    val iqamahConfigs: StateFlow<List<IqamahConfig>> = repository.iqamahConfigsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setCityId(cityId: Int) {
        viewModelScope.launch {
            repository.setCityId(cityId)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }

    fun setIqamahTimesEnabled(iqamahTimesEnabled: Boolean) {
        viewModelScope.launch {
            repository.setIqamahTimesEnabled(iqamahTimesEnabled)
        }
    }

    fun addCompletedSetupStep(setupStep: SetupStep) {
        viewModelScope.launch {
            repository.addCompletedSetupStep(setupStep)
        }
    }

    fun addCompletedSetupSteps(setupSteps: List<SetupStep>) {
        viewModelScope.launch {
            repository.addCompletedSetupSteps(setupSteps)
        }
    }

    fun setIqamahConfig(config: IqamahConfig) {
        viewModelScope.launch {
            repository.setIqamahConfig(config)
        }
    }
}
