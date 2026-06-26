package com.codealyst.omanprayertimes.ui.screens.prayer_times.setup_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

val setupSteps = listOf(
    SetupStep.LANGUAGE,
    SetupStep.CITY,
    SetupStep.THEME,
    SetupStep.CONFIGURE_IQAMAH_TIMES,
)

@Composable
fun SetupDialog(
    modifier: Modifier = Modifier,
    onConfigureIqamahTimes: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var stepIndex by rememberSaveable { mutableIntStateOf(0) }

    val colorScheme = MaterialTheme.colorScheme;
    val fonts = MaterialTheme.typography;

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = modifier
                .background(colorScheme.surfaceContainerHigh, RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Setup",
                        style = fonts.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider()

                Box(
                    Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                ) {
                    when (setupSteps[stepIndex]) {
                        SetupStep.LANGUAGE -> LanguageStep()
                        SetupStep.CITY -> CityStep()
                        SetupStep.THEME -> ThemeSetup()
                        SetupStep.CONFIGURE_IQAMAH_TIMES -> IqamahConfigsStep(onConfigureIqamahTimes = onConfigureIqamahTimes)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (stepIndex > 0) {
                            stepIndex--
                        }
                    }, enabled = stepIndex > 0) {
                        Text("Back")
                    }

                    TextButton(onClick = {
                        if ((stepIndex + 1) >= setupSteps.size) {
                            onDismissRequest()
                        } else {
                            stepIndex++
                        }
                    }) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

enum class SetupStep {
    LANGUAGE,
    CITY,
    THEME,
    CONFIGURE_IQAMAH_TIMES
}