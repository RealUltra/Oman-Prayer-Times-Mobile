package com.codealyst.omanprayertimes.ui.screens.prayer_times.initial_setup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownWidth
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun ThemeSetup(modifier: Modifier = Modifier) {
    val themeLabels = stringArrayResource(R.array.themes)

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    SetupStepContent(
        title = stringResource(R.string.theme_setup_title),
        description = stringResource(R.string.theme_setup_description),
        modifier = modifier
    ) {
        Dropdown(
            options = listOf(
                DropdownOptions(themeLabels[0], ""),
                DropdownOptions(themeLabels[1], "light"),
                DropdownOptions(themeLabels[2], "dark")
            ),
            modifier = Modifier.fillMaxWidth(),
            width = DropdownWidth.Fill,
            selectedValue = settings.theme,
            onOptionSelected = { theme -> settingsViewModel.setTheme(theme) },
        )
    }
}
