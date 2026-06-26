package com.codealyst.omanprayertimes.ui.screens.prayer_times.initial_setup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.dtos.City
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.DropdownOptions
import com.codealyst.omanprayertimes.ui.components.SearchableDropdown

@Composable
fun CityStep(modifier: Modifier = Modifier) {
    // Retrieve cities list
    var cityNames = stringArrayResource(R.array.city_names)
    var cityIds = integerArrayResource(R.array.city_ids)
    val citiesList = cityNames.toCityDtos(cityIds)

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    SetupStepContent(
        title = stringResource(R.string.city_setup_title),
        description = stringResource(R.string.city_setup_description),
        modifier = modifier
    ) {
        SearchableDropdown(
            options = citiesList.map { c -> DropdownOptions(c.cityName, c.cityId) },
            modifier = Modifier.fillMaxWidth(),
            selectedValue = settings.cityId,
            onOptionSelected = { cityId -> settingsViewModel.setCityId(cityId) },
        )
    }
}

private fun Array<String>.toCityDtos(cityIds: IntArray): List<City> {
    require(size == cityIds.size)
    return mapIndexed { index, cityName ->
        City(
            cityName = cityName,
            cityId = cityIds[index]
        )
    }.sortedBy { it.cityName }
}
