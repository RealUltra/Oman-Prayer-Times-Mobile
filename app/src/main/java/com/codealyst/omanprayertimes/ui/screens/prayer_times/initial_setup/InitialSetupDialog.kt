package com.codealyst.omanprayertimes.ui.screens.prayer_times.initial_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.SetupStep

val setupSteps = listOf(
    SetupStep.LANGUAGE,
    SetupStep.CITY,
    SetupStep.THEME,
    SetupStep.CONFIGURE_IQAMAH_TIMES,
)

@Composable
fun InitialSetupDialog(
    modifier: Modifier = Modifier,
    incompleteSetupSteps: List<SetupStep>,
    onConfigureIqamahTimes: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var stepIndex by rememberSaveable { mutableIntStateOf(0) }

    val fonts = MaterialTheme.typography;

    Dialog(onDismissRequest = {}) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.initial_setup_title),
                        style = fonts.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Start,
                    )

                    Text(
                        stringResource(
                            R.string.setup_step_progress,
                            stepIndex + 1,
                            incompleteSetupSteps.size
                        ),
                        style = fonts.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    when (incompleteSetupSteps[stepIndex]) {
                        SetupStep.LANGUAGE -> LanguageStep()
                        SetupStep.CITY -> CityStep()
                        SetupStep.THEME -> ThemeSetup()
                        SetupStep.CONFIGURE_IQAMAH_TIMES -> IqamahConfigsStep(onConfigureIqamahTimes = onConfigureIqamahTimes)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (stepIndex > 0) {
                            stepIndex--
                        }
                    }, enabled = stepIndex > 0) {
                        Text(stringResource(R.string.back))
                    }

                    Spacer(Modifier.width(4.dp))

                    Button(onClick = {
                        if ((stepIndex + 1) >= incompleteSetupSteps.size) {
                            onDismissRequest()
                        } else {
                            stepIndex++
                        }
                    }) {
                        Text(
                            if (stepIndex < (incompleteSetupSteps.size - 1)) {
                                stringResource(R.string.next)
                            } else {
                                stringResource(R.string.complete)
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getIncompleteSetupSteps(completedSetupSteps: List<String>): List<SetupStep> {
    return setupSteps.filter { setupStep -> !completedSetupSteps.contains(setupStep.name) }
}
