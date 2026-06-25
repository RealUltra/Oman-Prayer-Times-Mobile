package com.codealyst.omanprayertimes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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


@Composable
fun <T> Dropdown(
    options: List<DropdownOptions<T>>,
    modifier: Modifier = Modifier,
    width: DropdownWidth = DropdownWidth.LargestOption,
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
        mutableStateOf(
            options.firstOrNull { it.value == selectedValue } ?: options.firstOrNull()
        )
    }

    val columnModifier =
        if (width == DropdownWidth.LargestOption) Modifier.width(IntrinsicSize.Max) else Modifier.fillMaxWidth()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = columnModifier
                .clickable(
                    enabled = enabled && options.isNotEmpty(),
                    onClick = { expanded = true }
                )
        ) {
            val contentColor = when {
                !enabled -> colorScheme.onSurface.copy(alpha = 0.38f)
                selectedOption == null -> colorScheme.onSurfaceVariant
                else -> collapsedTextColor
            }

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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.name,
                            style = expandedTextStyle,
                            color = expandedTextColor
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected?.invoke(option.value)
                    }
                )
            }
        }
    }
}

enum class DropdownWidth {
    LargestOption,
    Fill
}
