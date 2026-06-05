package com.codealyst.omanprayertimes.ui.screens.iqamah_times

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.features.settings.IqamahMode
import com.codealyst.omanprayertimes.ui.components.SegmentedControl
import com.codealyst.omanprayertimes.ui.components.SegmentedControlOption

@Composable
fun TimeSelector(
    mode: String,
    initialMinutesAfterAdhan: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var minutesAfterAdhan by remember(initialMinutesAfterAdhan) {
        mutableStateOf(initialMinutesAfterAdhan.filter { it.isDigit() })
    }
    var exactHour by remember { mutableStateOf("7") }
    var exactMinute by remember { mutableStateOf("15") }
    var exactPeriod by remember { mutableStateOf("PM") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (mode == IqamahMode.AFTER_ADHAN) {
            MinutesAfterAdhanInput(
                value = minutesAfterAdhan,
                enabled = enabled,
                onValueChange = { minutesAfterAdhan = it }
            )
        } else {
            ExactTimeInput(
                hour = exactHour,
                minute = exactMinute,
                period = exactPeriod,
                enabled = enabled,
                onHourChange = { exactHour = it },
                onMinuteChange = { exactMinute = it },
                onPeriodChange = { exactPeriod = it }
            )
        }
    }
}

@Composable
private fun MinutesAfterAdhanInput(
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { text ->
                if (text.length <= 3 && text.all { it.isDigit() }) {
                    onValueChange(text)
                }
            },
            modifier = Modifier.width(112.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("min") }
        )

        Text(
            "after adhan",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ExactTimeInput(
    hour: String,
    minute: String,
    period: String,
    enabled: Boolean,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onPeriodChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        OutlinedTextField(
            value = hour,
            onValueChange = { text ->
                if (text.length <= 2 && text.all { it.isDigit() }) {
                    onHourChange(text)
                }
            },
            modifier = Modifier.width(58.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("7") }
        )

        Text(":")

        OutlinedTextField(
            value = minute,
            onValueChange = { text ->
                if (text.length <= 2 && text.all { it.isDigit() }) {
                    onMinuteChange(text)
                }
            },
            modifier = Modifier.width(58.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("15") }
        )

        SegmentedControl(
            options = listOf(
                SegmentedControlOption("AM", "AM"),
                SegmentedControlOption("PM", "PM")
            ),
            selectedValue = period,
            modifier = Modifier.widthIn(min = 112.dp),
            enabled = enabled,
            onOptionSelected = onPeriodChange
        )
    }
}
