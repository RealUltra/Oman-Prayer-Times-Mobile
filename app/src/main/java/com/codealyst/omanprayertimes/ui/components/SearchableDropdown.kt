package com.codealyst.omanprayertimes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> SearchableDropdown(
    options: List<DropdownOptions<T>>,
    modifier: Modifier = Modifier,
    selectedValue: T? = options.firstOrNull()?.value,
    enabled: Boolean = true,
    placeholder: String = "",
    collapsedTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    expandedTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    collapsedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    expandedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    onOptionSelected: ((T) -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    var selectedOption by remember(options, selectedValue) {
        mutableStateOf(options.firstOrNull { it.value == selectedValue } ?: options.firstOrNull())
    }

    val contentColor = when {
        !enabled -> colorScheme.onSurface.copy(alpha = 0.38f)
        selectedOption == null -> colorScheme.onSurfaceVariant
        else -> collapsedTextColor
    }

    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .clickable(
                enabled = enabled && options.isNotEmpty(),
                onClick = { expanded = true }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedOption?.name ?: placeholder,
                modifier = Modifier.weight(1f),
                style = collapsedTextStyle,
                color = contentColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            DropdownArrow(color = contentColor)
        }

        Spacer(modifier = Modifier.height(2.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = if (enabled) colorScheme.outline else Color.Transparent
        )
    }

    if (expanded) {
        ExpandedSearchableDropdownMenu(
            options = options,
            expandedTextStyle = expandedTextStyle,
            expandedTextColor = expandedTextColor,
            onDismissRequest = { expanded = false },
            onOptionSelected = { option ->
                selectedOption = option
                expanded = false
                onOptionSelected?.invoke(option.value)
            }
        )
    }
}

@Composable
fun <T> ExpandedSearchableDropdownMenu(
    options: List<DropdownOptions<T>>,
    modifier: Modifier = Modifier,
    searchPlaceholder: String = "Search",
    expandedTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    expandedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    onDismissRequest: () -> Unit,
    onOptionSelected: (DropdownOptions<T>) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredOptions = remember(options, searchText) {
        if (searchText.isBlank()) {
            options
        } else {
            options.filter { option ->
                option.name.contains(searchText, ignoreCase = true)
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(searchPlaceholder) }
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 320.dp)
                ) {
                    items(filteredOptions) { option ->
                        Text(
                            text = option.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOptionSelected(option) }
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            style = expandedTextStyle,
                            color = expandedTextColor
                        )
                    }

                    if (filteredOptions.isEmpty()) {
                        item {
                            Text(
                                text = "No results",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
