package com.codealyst.omanprayertimes.ui.screens.prayer_times.setup_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun ThemeSetup(modifier: Modifier = Modifier) {
    val themeLabels = stringArrayResource(R.array.themes)

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val fonts = MaterialTheme.typography

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            stringResource(R.string.theme) + ":",
            style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )

        Dropdown(
            options = listOf(
                DropdownOptions(themeLabels[0], ""),
                DropdownOptions(themeLabels[1], "light"),
                DropdownOptions(themeLabels[2], "dark")
            ),
            selectedValue = settings.theme,
            onOptionSelected = { theme -> settingsViewModel.setTheme(theme) },
        )
    }
}
