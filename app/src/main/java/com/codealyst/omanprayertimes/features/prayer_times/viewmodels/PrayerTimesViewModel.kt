package com.codealyst.omanprayertimes.features.prayer_times.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.api.dtos.PrayerTimesByDate
import com.codealyst.omanprayertimes.features.prayer_times.PrayerTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(private val repository: PrayerTimesRepository) :
    ViewModel() {
    private val _state: MutableState<UiState<PrayerTimesByDate>> =
        mutableStateOf(UiState.Loading)
    val state: State<UiState<PrayerTimesByDate>> = _state

    fun fetchYearlyPrayerTimes(cityId: Int = 0) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            
            try {
                val response = repository.getYearlyPrayerTimes(cityId)
                _state.value = UiState.Success(response)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getPrayerTimesForDate(date: LocalDate): DailyPrayerTimes? {
        if (_state.value !is UiState.Success) {
            return null
        }

        val prayerTimesByDate = (_state.value as UiState.Success<PrayerTimesByDate>).data

        if (prayerTimesByDate.isEmpty()) {
            return null
        }

        val firstDateKey = prayerTimesByDate.keys.first()
        val year = firstDateKey.substringBefore("-").toInt()

        val dateKey = date.withYearSafe(year).toString()

        return prayerTimesByDate.get(dateKey)
    }
}

fun LocalDate.withYearSafe(targetYear: Int): LocalDate {
    return try {
        this.withYear(targetYear)
    } catch (_: DateTimeException) {
        // Handle invalid date (e.g., February 29 on a non-leap year)
        LocalDate.of(targetYear, this.month, this.dayOfMonth.coerceAtMost(28))
    }
}
