package com.codealyst.omanprayertimes.features.prayertimes.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codealyst.omanprayertimes.features.api.dtos.City
import com.codealyst.omanprayertimes.features.prayertimes.PrayerTimesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CitiesViewModel @Inject constructor(private val repository: PrayerTimesRepository) :
    ViewModel() {
    private val _state: MutableState<UiState<List<City>>> =
        mutableStateOf(UiState.Loading)
    val state: State<UiState<List<City>>> = _state

    init {
        fetchCities()
    }

    fun fetchCities(cityId: Int? = null) {
        viewModelScope.launch {
            try {
                val response = repository.getCities(cityId)
                _state.value = UiState.Success(response)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
