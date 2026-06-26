package com.codealyst.omanprayertimes.ui.screens.prayer_times.initial_setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel

@Composable
fun IqamahConfigsStep(modifier: Modifier = Modifier, onConfigureIqamahTimes: () -> Unit) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val fonts = MaterialTheme.typography

    SetupStepContent(
        title = stringResource(R.string.iqamah_setup_title),
        description = stringResource(R.string.iqamah_setup_description),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.show_iqamah_times),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                Switch(
                    checked = settings.iqamahTimesEnabled,
                    onCheckedChange = { enabled ->
                        settingsViewModel.setIqamahTimesEnabled(enabled)
                    }
                )
            }

            if (settings.iqamahTimesEnabled) {
                OutlinedButton(
                    onClick = onConfigureIqamahTimes,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.configure_iqamah_times),
                        style = fonts.bodyMedium
                    )
                }
            }
        }
    }
}
