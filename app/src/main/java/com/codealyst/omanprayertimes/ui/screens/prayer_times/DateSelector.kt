package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.oman_datetime.getOmanDate
import com.codealyst.omanprayertimes.ui.components.SegmentedControl
import com.codealyst.omanprayertimes.ui.components.SegmentedControlOption
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val today = remember { getOmanDate() }
    val tomorrow = remember(today) { today.plusDays(1) }
    var selectedDateText by rememberSaveable { mutableStateOf(today.toString()) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val selectedDate = remember(selectedDateText) { LocalDate.parse(selectedDateText) }
    val selectedOption = when (selectedDate) {
        today -> DateSelectorOption.Today
        tomorrow -> DateSelectorOption.Tomorrow
        else -> DateSelectorOption.Custom
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SegmentedControl(
                options = DateSelectorOption.entries.map { option ->
                    SegmentedControlOption(stringResource(option.labelRes), option)
                },
                selectedValue = selectedOption,
                modifier = Modifier.fillMaxWidth(),
                onOptionSelected = { option ->
                    when (option) {
                        DateSelectorOption.Today -> {
                            selectedDateText = today.toString()
                            onDateSelected(today)
                        }

                        DateSelectorOption.Tomorrow -> {
                            selectedDateText = tomorrow.toString()
                            onDateSelected(tomorrow)
                        }

                        DateSelectorOption.Custom -> {
                            showDatePicker = true
                        }
                    }
                }
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toDatePickerMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val pickedDate = selectedMillis.toDatePickerLocalDate()
                            selectedDateText = pickedDate.toString()
                            onDateSelected(pickedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.use_date))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private enum class DateSelectorOption(@StringRes val labelRes: Int) {
    Today(R.string.today),
    Tomorrow(R.string.tomorrow),
    Custom(R.string.custom_date),
}

private val selectedDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE, d MMM yyyy")

private fun LocalDate.toDatePickerMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toDatePickerLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
