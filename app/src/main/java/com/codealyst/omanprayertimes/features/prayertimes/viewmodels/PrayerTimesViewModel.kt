package com.codealyst.omanprayertimes.features.prayertimes.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.prayertimes.PrayerTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(private val repository: PrayerTimesRepository) :
    ViewModel() {
    private val _state: MutableState<UiState<DailyPrayerTimes>> =
        mutableStateOf(UiState.Loading)
    val state: State<UiState<DailyPrayerTimes>> = _state

    fun fetchPrayerTimesForDate(date: LocalDate, cityId: Int = 0) {
        viewModelScope.launch {
            try {
                val response = repository.getPrayerTimesForDate(date, cityId)
                _state.value = UiState.Success(response)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
