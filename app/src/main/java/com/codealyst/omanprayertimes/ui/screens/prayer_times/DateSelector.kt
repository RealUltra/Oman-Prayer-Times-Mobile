package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.oman_datetime.getOmanDate
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    today: LocalDate = getOmanDate(),
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val tomorrow = today.plusDays(1)

    var selectedDate by rememberSaveable { mutableStateOf(today) }
    var selectedOption by rememberSaveable { mutableStateOf(DateSelectorOption.Today) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    // How a change of date is handled
    LaunchedEffect(today) {
        // If "Today" is selected, move ahead to the new date.
        if (selectedOption == DateSelectorOption.Today) {
            selectedDate = today
        }
        // If "Tomorrow" was selected, which has now become today, update the option to today.
        else if (selectedDate == today) {
            selectedOption = DateSelectorOption.Today
        }
        // If a custom date was selected, which has now become tomorrow, select "Tomorrow".
        else if (selectedDate == tomorrow) {
            selectedOption = DateSelectorOption.Tomorrow
        }
        onDateSelected(today)
    }

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .background(colorScheme.surface, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                stringResource(R.string.prayer_times_for),
                style = fonts.bodyLarge.copy(
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                ),
            )

            Dropdown(
                options = listOf(
                    DropdownOptions(
                        stringResource(DateSelectorOption.Today.labelRes),
                        DateSelectorOption.Today
                    ),
                    DropdownOptions(
                        stringResource(DateSelectorOption.Tomorrow.labelRes),
                        DateSelectorOption.Tomorrow
                    ),
                    DropdownOptions(
                        stringResource(DateSelectorOption.Custom.labelRes),
                        DateSelectorOption.Custom
                    ),
                ),
                selectedValue = selectedOption,

                onOptionSelected = { option ->
                    if (option == DateSelectorOption.Today) {
                        selectedOption = option
                        selectedDate = today
                        onDateSelected(today)
                    } else if (option == DateSelectorOption.Tomorrow) {
                        selectedOption = option
                        selectedDate = tomorrow
                        onDateSelected(selectedDate)
                    } else {
                        showDatePicker = true
                    }
                },

                collapsedTextStyle = fonts.bodyLarge,
                expandedTextStyle = fonts.bodyLarge,
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = selectedDate,
            onDateSelected = { pickedDate ->
                selectedOption = when (pickedDate) {
                    today -> DateSelectorOption.Today
                    tomorrow -> DateSelectorOption.Tomorrow
                    else -> DateSelectorOption.Custom
                }
                selectedDate = pickedDate
                onDateSelected(pickedDate)
            },
            onDismissRequest = {
                showDatePicker = false
            }
        )
    }
}

private enum class DateSelectorOption(@StringRes val labelRes: Int) {
    Today(R.string.today),
    Tomorrow(R.string.tomorrow),
    Custom(R.string.custom_date),
}
