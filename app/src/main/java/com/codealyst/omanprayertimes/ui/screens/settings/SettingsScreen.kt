package com.codealyst.omanprayertimes.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.codealyst.omanprayertimes.BuildConfig
import com.codealyst.omanprayertimes.features.api.dtos.City
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.CitiesViewModel
import com.codealyst.omanprayertimes.features.prayer_times.viewmodels.UiState
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions
import com.codealyst.omanprayertimes.ui.components.ScreenHeader
import com.codealyst.omanprayertimes.ui.components.SearchableDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    // Retrieve cities list
    val citiesViewModel = hiltViewModel<CitiesViewModel>()
    val state = citiesViewModel.state.value
    val citiesList: List<City> = if (state is UiState.Success) state.data else emptyList();

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(title = "Settings")

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsGroup(title = "General") {
                SettingsRow(title = "City") {
                    SearchableDropdown(
                        modifier = Modifier.width(100.dp),
                        options = citiesList.map { c -> DropdownOptions(c.cityName, c.cityId) },
                        selectedValue = settings.cityId,
                        onOptionSelected = { cityId -> settingsViewModel.setCityId(cityId) },
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = "Language") {
                    Dropdown(
                        options = listOf(
                            DropdownOptions("Default", ""),
                            DropdownOptions("English", "en"),
                            DropdownOptions("Arabic", "ar"),
                        ),
                        selectedValue = settings.language,
                        onOptionSelected = { language -> settingsViewModel.setLanguage(language) },
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = "Theme") {
                    Dropdown(
                        options = listOf(
                            DropdownOptions("Default", ""),
                            DropdownOptions("Light", "light"),
                            DropdownOptions("Dark", "dark")
                        ),
                        selectedValue = settings.theme,
                        onOptionSelected = { theme -> settingsViewModel.setTheme(theme) },
                    )
                }
            }

            SettingsGroup(title = "Prayer Setup") {
                SettingsRow(title = "Iqamah Times") {
                    Text(
                        "Configure",
                        style = fonts.bodyMedium,
                        color = colorScheme.primary,
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = { navController.navigate("settings/iqamah_times") })
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = "Reminders") {
                    Text(
                        "Configure",
                        style = fonts.bodyMedium,
                        color = colorScheme.primary,
                    )
                }
            }

            SettingsGroup(title = "About") {
                SettingsRow(title = "Version") {
                    Text(
                        "v${BuildConfig.VERSION_NAME}",
                        style = fonts.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
