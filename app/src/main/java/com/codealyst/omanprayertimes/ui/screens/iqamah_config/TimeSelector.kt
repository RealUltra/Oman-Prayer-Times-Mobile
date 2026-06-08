package com.codealyst.omanprayertimes.ui.screens.iqamah_config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import com.codealyst.omanprayertimes.ui.components.SegmentedControl
import com.codealyst.omanprayertimes.ui.components.SegmentedControlOption

@Composable
fun TimeSelector(
    mode: String,
    minutesAfterAdhan: String,
    exactTime: String,
    onMinutesAfterAdhanChange: (String) -> Unit,
    onExactTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialExactTimeParts = remember(exactTime) {
        parseInitialExactTime(exactTime)
    }
    var exactHour by remember(exactTime) {
        mutableStateOf(initialExactTimeParts.hour)
    }
    var exactMinute by remember(exactTime) {
        mutableStateOf(initialExactTimeParts.minute)
    }
    var exactPeriod by remember(exactTime) {
        mutableStateOf(initialExactTimeParts.period)
    }

    fun updateExactTime(
        nextHour: String = exactHour,
        nextMinute: String = exactMinute,
        nextPeriod: TimePeriod = exactPeriod
    ) {
        toTwentyFourHourTime(nextHour, nextMinute, nextPeriod)?.let(onExactTimeChange)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (mode == IqamahMode.AFTER_ADHAN) {
            MinutesAfterAdhanInput(
                value = minutesAfterAdhan,
                onValueChange = onMinutesAfterAdhanChange
            )
        } else {
            ExactTimeInput(
                hour = exactHour,
                minute = exactMinute,
                period = exactPeriod,
                onHourChange = {
                    exactHour = it
                    updateExactTime(nextHour = it)
                },
                onMinuteChange = {
                    exactMinute = it
                    updateExactTime(nextMinute = it)
                },
                onPeriodChange = {
                    exactPeriod = it
                    updateExactTime(nextPeriod = it)
                }
            )
        }
    }
}

private data class ExactTimeParts(
    val hour: String,
    val minute: String,
    val period: TimePeriod
)

private fun parseInitialExactTime(initialExactTime: String): ExactTimeParts {
    val parts = initialExactTime.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull()
    val minute = parts.getOrNull(1)?.toIntOrNull()

    if (hour == null || minute == null || hour !in 0..23 || minute !in 0..59) {
        return ExactTimeParts(hour = "0", minute = "00", period = TimePeriod.AM)
    }

    val hour12 = when (val value = hour % 12) {
        0 -> 12
        else -> value
    }
    val period = if (hour < 12) TimePeriod.AM else TimePeriod.PM

    return ExactTimeParts(
        hour = hour12.toString(),
        minute = minute.toString().padStart(2, '0'),
        period = period
    )
}

private fun toTwentyFourHourTime(
    hourText: String,
    minuteText: String,
    period: TimePeriod
): String? {
    val hour = hourText.toIntOrNull()
    val minute = minuteText.toIntOrNull()

    if (hour == null || minute == null || hour !in 1..12 || minute !in 0..59) {
        return null
    }

    val hour24 = when (period) {
        TimePeriod.AM -> if (hour == 12) 0 else hour
        TimePeriod.PM -> if (hour == 12) 12 else hour + 12
    }

    return "${hour24.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}

@Composable
private fun MinutesAfterAdhanInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { text ->
                if (text.length <= 3 && text.all { it.isDigit() }) {
                    onValueChange(text)
                }
            },
            modifier = Modifier
                .width(104.dp)
                .heightIn(min = 56.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = fonts.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            shape = RoundedCornerShape(10.dp),
            colors = compactTextFieldColors(),
            suffix = {
                Text(
                    stringResource(R.string.minute_abbreviation),
                    style = fonts.labelLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
        )

        Text(
            stringResource(R.string.minutes_after_adhan),
            style = fonts.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ExactTimeInput(
    hour: String,
    minute: String,
    period: TimePeriod,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onPeriodChange: (TimePeriod) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = hour,
            onValueChange = { text ->
                if (text.length <= 2 && text.all { it.isDigit() }) {
                    onHourChange(text)
                }
            },
            modifier = Modifier
                .width(64.dp)
                .heightIn(min = 56.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = fonts.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            shape = RoundedCornerShape(10.dp),
            colors = compactTextFieldColors(),
            placeholder = { Text(stringResource(R.string.exact_hour_placeholder)) }
        )

        Text(
            ":",
            style = fonts.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = minute,
            onValueChange = { text ->
                if (text.length <= 2 && text.all { it.isDigit() }) {
                    onMinuteChange(text)
                }
            },
            modifier = Modifier
                .width(64.dp)
                .heightIn(min = 56.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = fonts.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            shape = RoundedCornerShape(10.dp),
            colors = compactTextFieldColors(),
            placeholder = { Text(stringResource(R.string.exact_minute_placeholder)) }
        )

        SegmentedControl(
            options = listOf(
                SegmentedControlOption(stringResource(R.string.am), TimePeriod.AM),
                SegmentedControlOption(stringResource(R.string.pm), TimePeriod.PM)
            ),
            selectedValue = period,
            modifier = Modifier.widthIn(min = 124.dp),
            labelStyle = fonts.bodyMedium,
            onOptionSelected = onPeriodChange
        )
    }
}

@Composable
private fun compactTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    cursorColor = MaterialTheme.colorScheme.primary
)

private enum class TimePeriod {
    AM, PM
}
